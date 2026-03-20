import { l as aa, n as Ai, E as me, B as oa, p as nn, q as Ne, M as _t, r as N, u as F, O as Ci, A as Bt, v as sa, w as Gt, x, y as la, c as X, z as ca, D as an, e as xi, F as _e, G as oe, H as da, I as ua, J as Di, K as ha, L as fa, N as Pt, Q as pa, R as K, S as on, T as ma, U as ga, V as Ae, W as Nt, X as Lt, Y as bn, Z as va, _ as Ea, $ as ba, a0 as Oi, C as Xt, a1 as wa, a2 as ya, a3 as Ra, a4 as $a, a5 as Ia, a6 as Sa, a7 as ze, P as Ta } from "./copilot-Do4z_cRr.js";
import { r as ve } from "./state-ZcKS_gmE.js";
import { f as Aa, c as et, a as _i } from "./repeat-CLWrioI0.js";
import { i as ne } from "./icons-D3bPNGv-.js";
/**
 * @license
 * Copyright (c) 2017 The Polymer Project Authors. All rights reserved.
 * This code may only be used under the BSD style license found at http://polymer.github.io/LICENSE.txt
 * The complete set of authors may be found at http://polymer.github.io/AUTHORS.txt
 * The complete set of contributors may be found at http://polymer.github.io/CONTRIBUTORS.txt
 * Code distributed by Google as part of the polymer project is also
 * subject to an additional IP rights grant found at http://polymer.github.io/PATENTS.txt
 */
let wn = 0, Pi = 0;
const Ue = [];
let Wr = !1;
function Ca() {
  Wr = !1;
  const e = Ue.length;
  for (let t = 0; t < e; t++) {
    const r = Ue[t];
    if (r)
      try {
        r();
      } catch (n) {
        setTimeout(() => {
          throw n;
        });
      }
  }
  Ue.splice(0, e), Pi += e;
}
const xa = {
  /**
   * Enqueues a function called at microtask timing.
   *
   * @memberof microTask
   * @param {!Function=} callback Callback to run
   * @return {number} Handle used for canceling task
   */
  run(e) {
    Wr || (Wr = !0, queueMicrotask(() => Ca())), Ue.push(e);
    const t = wn;
    return wn += 1, t;
  },
  /**
   * Cancels a previously enqueued `microTask` callback.
   *
   * @memberof microTask
   * @param {number} handle Handle returned from `run` of callback to cancel
   * @return {void}
   */
  cancel(e) {
    const t = e - Pi;
    if (t >= 0) {
      if (!Ue[t])
        throw new Error(`invalid async handle: ${e}`);
      Ue[t] = null;
    }
  }
};
/**
@license
Copyright (c) 2017 The Polymer Project Authors. All rights reserved.
This code may only be used under the BSD style license found at http://polymer.github.io/LICENSE.txt
The complete set of authors may be found at http://polymer.github.io/AUTHORS.txt
The complete set of contributors may be found at http://polymer.github.io/CONTRIBUTORS.txt
Code distributed by Google as part of the polymer project is also
subject to an additional IP rights grant found at http://polymer.github.io/PATENTS.txt
*/
const yn = /* @__PURE__ */ new Set();
class $t {
  /**
   * Creates a debouncer if no debouncer is passed as a parameter
   * or it cancels an active debouncer otherwise. The following
   * example shows how a debouncer can be called multiple times within a
   * microtask and "debounced" such that the provided callback function is
   * called once. Add this method to a custom element:
   *
   * ```js
   * import {microTask} from '@vaadin/component-base/src/async.js';
   * import {Debouncer} from '@vaadin/component-base/src/debounce.js';
   * // ...
   *
   * _debounceWork() {
   *   this._debounceJob = Debouncer.debounce(this._debounceJob,
   *       microTask, () => this._doWork());
   * }
   * ```
   *
   * If the `_debounceWork` method is called multiple times within the same
   * microtask, the `_doWork` function will be called only once at the next
   * microtask checkpoint.
   *
   * Note: In testing it is often convenient to avoid asynchrony. To accomplish
   * this with a debouncer, you can use `enqueueDebouncer` and
   * `flush`. For example, extend the above example by adding
   * `enqueueDebouncer(this._debounceJob)` at the end of the
   * `_debounceWork` method. Then in a test, call `flush` to ensure
   * the debouncer has completed.
   *
   * @param {Debouncer?} debouncer Debouncer object.
   * @param {!AsyncInterface} asyncModule Object with Async interface
   * @param {function()} callback Callback to run.
   * @return {!Debouncer} Returns a debouncer object.
   */
  static debounce(t, r, n) {
    return t instanceof $t ? t._cancelAsync() : t = new $t(), t.setConfig(r, n), t;
  }
  constructor() {
    this._asyncModule = null, this._callback = null, this._timer = null;
  }
  /**
   * Sets the scheduler; that is, a module with the Async interface,
   * a callback and optional arguments to be passed to the run function
   * from the async module.
   *
   * @param {!AsyncInterface} asyncModule Object with Async interface.
   * @param {function()} callback Callback to run.
   * @return {void}
   */
  setConfig(t, r) {
    this._asyncModule = t, this._callback = r, this._timer = this._asyncModule.run(() => {
      this._timer = null, yn.delete(this), this._callback();
    });
  }
  /**
   * Cancels an active debouncer and returns a reference to itself.
   *
   * @return {void}
   */
  cancel() {
    this.isActive() && (this._cancelAsync(), yn.delete(this));
  }
  /**
   * Cancels a debouncer's async callback.
   *
   * @return {void}
   */
  _cancelAsync() {
    this.isActive() && (this._asyncModule.cancel(
      /** @type {number} */
      this._timer
    ), this._timer = null);
  }
  /**
   * Flushes an active debouncer and returns a reference to itself.
   *
   * @return {void}
   */
  flush() {
    this.isActive() && (this.cancel(), this._callback());
  }
  /**
   * Returns true if the debouncer is active.
   *
   * @return {boolean} True if active.
   */
  isActive() {
    return this._timer != null;
  }
}
/**
 * @license
 * Copyright 2017 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */
const tt = (e, t) => {
  const r = e._$AN;
  if (r === void 0) return !1;
  for (const n of r) n._$AO?.(t, !1), tt(n, t);
  return !0;
}, It = (e) => {
  let t, r;
  do {
    if ((t = e._$AM) === void 0) break;
    r = t._$AN, r.delete(e), e = t;
  } while (r?.size === 0);
}, Ni = (e) => {
  for (let t; t = e._$AM; e = t) {
    let r = t._$AN;
    if (r === void 0) t._$AN = r = /* @__PURE__ */ new Set();
    else if (r.has(e)) break;
    r.add(e), _a(t);
  }
};
function Da(e) {
  this._$AN !== void 0 ? (It(this), this._$AM = e, Ni(this)) : this._$AM = e;
}
function Oa(e, t = !1, r = 0) {
  const n = this._$AH, i = this._$AN;
  if (i !== void 0 && i.size !== 0) if (t) if (Array.isArray(n)) for (let a = r; a < n.length; a++) tt(n[a], !1), It(n[a]);
  else n != null && (tt(n, !1), It(n));
  else tt(this, e);
}
const _a = (e) => {
  e.type == Ai.CHILD && (e._$AP ??= Oa, e._$AQ ??= Da);
};
class Pa extends aa {
  constructor() {
    super(...arguments), this._$AN = void 0;
  }
  _$AT(t, r, n) {
    super._$AT(t, r, n), Ni(this), this.isConnected = t._$AU;
  }
  _$AO(t, r = !0) {
    t !== this.isConnected && (this.isConnected = t, t ? this.reconnected?.() : this.disconnected?.()), r && (tt(this, t), It(this));
  }
  setValue(t) {
    if (Aa(this._$Ct)) this._$Ct._$AI(t, this);
    else {
      const r = [...this._$Ct._$AH];
      r[this._$Ci] = t, this._$Ct._$AI(r, this, 0);
    }
  }
  disconnected() {
  }
  reconnected() {
  }
}
/**
 * @license
 * Copyright (c) 2016 - 2025 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */
const Rn = Symbol("valueNotInitialized");
class Na extends Pa {
  constructor(t) {
    if (super(t), t.type !== Ai.ELEMENT)
      throw new Error(`\`${this.constructor.name}\` must be bound to an element.`);
    this.previousValue = Rn;
  }
  /** @override */
  render(t, r) {
    return me;
  }
  /** @override */
  update(t, [r, n]) {
    return this.hasChanged(n) ? (this.host = t.options && t.options.host, this.element = t.element, this.renderer = r, this.previousValue === Rn ? this.addRenderer() : this.runRenderer(), this.previousValue = Array.isArray(n) ? [...n] : n, me) : me;
  }
  /** @override */
  reconnected() {
    this.addRenderer();
  }
  /** @override */
  disconnected() {
    this.removeRenderer();
  }
  /** @abstract */
  addRenderer() {
    throw new Error("The `addRenderer` method must be implemented.");
  }
  /** @abstract */
  runRenderer() {
    throw new Error("The `runRenderer` method must be implemented.");
  }
  /** @abstract */
  removeRenderer() {
    throw new Error("The `removeRenderer` method must be implemented.");
  }
  /** @protected */
  renderRenderer(t, ...r) {
    const n = this.renderer.call(this.host, ...r);
    oa(n, t, { host: this.host });
  }
  /** @protected */
  hasChanged(t) {
    return Array.isArray(t) ? !Array.isArray(this.previousValue) || this.previousValue.length !== t.length ? !0 : t.some((r, n) => r !== this.previousValue[n]) : this.previousValue !== t;
  }
}
/**
 * @license
 * Copyright (c) 2017 - 2025 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */
const Wt = Symbol("contentUpdateDebouncer");
class sn extends Na {
  /**
   * A property to that the renderer callback will be assigned.
   *
   * @abstract
   */
  get rendererProperty() {
    throw new Error("The `rendererProperty` getter must be implemented.");
  }
  /**
   * Adds the renderer callback to the dialog.
   */
  addRenderer() {
    this.element[this.rendererProperty] = (t, r) => {
      this.renderRenderer(t, r);
    };
  }
  /**
   * Runs the renderer callback on the dialog.
   */
  runRenderer() {
    this.element[Wt] = $t.debounce(
      this.element[Wt],
      xa,
      () => {
        this.element.requestContentUpdate();
      }
    );
  }
  /**
   * Removes the renderer callback from the dialog.
   */
  removeRenderer() {
    this.element[this.rendererProperty] = null, delete this.element[Wt];
  }
}
class La extends sn {
  get rendererProperty() {
    return "renderer";
  }
}
class Ma extends sn {
  get rendererProperty() {
    return "headerRenderer";
  }
}
class Ha extends sn {
  get rendererProperty() {
    return "footerRenderer";
  }
}
const Mt = nn(La), Ht = nn(Ma), Vt = nn(Ha);
var Va = Object.defineProperty, qa = Object.getOwnPropertyDescriptor, ln = (e, t, r, n) => {
  for (var i = n > 1 ? void 0 : n ? qa(t, r) : t, a = e.length - 1, o; a >= 0; a--)
    (o = e[a]) && (i = (n ? o(t, r, i) : o(i)) || i);
  return n && i && Va(t, r, i), i;
};
let St = class extends _t {
  constructor() {
    super(...arguments), this.panelHeaderUpdated = 0, this.openedPanelConfigurationsSorted = [], this.windowResizeListener = async () => {
      await Promise.all(
        this.getOpenPanelContainerDialogs().map(async (e) => {
          const t = e.getAttribute("data-panel-tag");
          if (!t)
            return;
          await e.querySelector(t)?.requestLayoutUpdate();
          const n = N.getPanelByTag(t);
          if (!n?.position)
            return;
          const i = e.getBoundingClientRect(), a = this.getViewportAdjustedPosition({
            ...n.position,
            width: n.position.width ?? i.width,
            height: n.position.height ?? i.height
          });
          e.setAttribute("top", `${a.top}`), e.setAttribute("left", `${a.left}`), e.setAttribute("width", `${a.width}`), e.setAttribute("height", `${a.height}`), N.updatePanel(
            n.tag,
            {
              position: a
            },
            !1
          );
        })
      );
    };
  }
  connectedCallback() {
    super.connectedCallback(), window.addEventListener("resize", this.windowResizeListener), this.reaction(
      () => F.operationInProgress,
      () => {
        Array.from(this.querySelectorAll("vaadin-dialog[panel-container]")).forEach((e) => {
          const t = e.hasAttribute("showWhileDragging");
          requestAnimationFrame(() => {
            e.toggleAttribute(
              "hiding-while-drag-and-drop",
              F.operationInProgress === Ci.DragAndDrop && !t && !this.hasDropTarget(e)
            );
          });
        });
      }
    ), this.reaction(
      () => N.getAttentionRequiredPanelConfiguration(),
      () => {
        const e = N.getAttentionRequiredPanelConfiguration(), t = this.getDialogByPanelTag(e?.tag);
        t && t.toggleAttribute(Bt, !0);
      }
    ), this.observeDisposer = sa(N.getCustomPanelHeaderMap(), () => {
      this.panelHeaderUpdated += 1;
    }), this.reaction(
      () => [
        N.panels,
        N.panelStackingOrder,
        N.getOpenPanelTags().size
      ],
      () => {
        const e = N.panels.filter((r) => N.isOpenedPanel(r.tag)), t = N.panelStackingOrder;
        this.openedPanelConfigurationsSorted = e.sort((r, n) => {
          const i = t.indexOf(r.tag), a = t.indexOf(n.tag);
          return i - a;
        });
      },
      { fireImmediately: !0 }
    );
  }
  disconnectedCallback() {
    super.disconnectedCallback(), window.removeEventListener("resize", this.windowResizeListener), this.observeDisposer && this.observeDisposer();
  }
  createRenderRoot() {
    return this;
  }
  render() {
    return x`
      ${et(
      this.openedPanelConfigurationsSorted,
      (e) => e.tag,
      (e) => x`<vaadin-dialog
            draggable
            modeless
            id="${e.tag}-dialog"
            waits-positioning
            ?individual="${e.individual}"
            .opened="${N.isOpenedPanel(e.tag)}"
            panel-container
            resizable
            @mousedown="${(t) => {
        N.moveStackingOrderToTop(e.tag);
      }}"
            @touchstart="${(t) => {
        N.moveStackingOrderToTop(e.tag);
      }}"
            data-panel-tag="${e.tag}"
            ?showWhileDragging="${e.showWhileDragging}"
            theme="no-padding"
            left="${e.position?.left}"
            top="${e.position?.top}"
            height=${e.position?.height}
            width=${e.position?.width}
            @resize="${(t) => {
        const { top: r, left: n, width: i, height: a } = t.detail, o = t.currentTarget, d = Number.parseFloat(i), c = Number.parseFloat(a), s = this.getViewportAdjustedPosition({
          top: Number.parseFloat(r),
          left: Number.parseFloat(n),
          width: Number.isFinite(d) ? d : o.getBoundingClientRect().width,
          height: Number.isFinite(c) ? c : o.getBoundingClientRect().height
        });
        o.setAttribute("top", `${s.top}`), o.setAttribute("left", `${s.left}`), o.setAttribute("width", `${s.width}`), o.setAttribute("height", `${s.height}`), N.updatePanel(e.tag, {
          position: s
        });
      }}"
            @dragged="${(t) => {
        const r = N.getPanelByTag(e.tag);
        if (!r)
          return;
        const n = t.currentTarget, i = n.getBoundingClientRect(), a = {
          top: Number.parseFloat(t.detail.top),
          left: Number.parseFloat(t.detail.left)
        }, o = this.getViewportAdjustedPosition({
          width: i.width,
          height: i.height,
          ...r.position,
          ...a
        });
        n.setAttribute("top", `${o.top}`), n.setAttribute("left", `${o.left}`), N.updatePanel(r.tag, {
          position: o
        });
      }}"
            @mouseenter="${(t) => {
        const r = t.target;
        r.hasAttribute(Bt) && N.clearAttention(), r.toggleAttribute(Bt, !1);
      }}"
            @opened-changed="${(t) => {
        const r = t.detail.value, n = t.currentTarget;
        r && (N.moveStackingOrderToTop(e.tag), (n.querySelector(e.tag)?.requestLayoutUpdate() ?? Promise.resolve()).finally(() => {
          setTimeout(() => {
            n.removeAttribute("waits-positioning");
          }, 50);
        }));
        const i = this.getToolbar();
        if (!i)
          return;
        const a = i.getButtonId(e), o = i.querySelector(`vaadin-button#${a}`);
        o && (r ? o.classList.add("selected") : o.classList.remove("selected"));
      }}"
            ${Ht(
        () => x`
                <h2 class="font-bold me-auto my-0 text-xs truncate uppercase" id="${e.tag}-title">
                  ${N.getPanelHeader(e)}
                  ${e.experimental ? x`<vaadin-icon slot="suffix" .svg="${ne.experiment}"></vaadin-icon>
                        <vaadin-tooltip slot="tooltip" text="Experimental feature"></vaadin-tooltip>` : me}
                </h2>
                ${e.actionsTag ? Gt(`<${e.actionsTag}></${e.actionsTag}>`) : me}
                ${e.helpUrl ? x`<vaadin-button
                      aria-label="More info about ${e.header}"
                      @click="${() => window.open(e.helpUrl, "_blank")}"
                      @mousedown="${(t) => t.stopPropagation()}"
                      theme="icon tertiary">
                      <vaadin-icon .svg="${ne.help}"></vaadin-icon>
                      <vaadin-tooltip slot="tooltip" text="More info about ${e.header}"></vaadin-tooltip>
                    </vaadin-button>` : me}
                <vaadin-button
                  aria-label="Close"
                  @click="${() => {
          const r = this.getDialogByPanelTag(e.tag)?.querySelector(e.tag);
          r?.requestClose ? r.requestClose(() => N.closePanel(e.tag)) : N.closePanel(e.tag);
        }}"
                  @mousedown="${(t) => t.stopPropagation()}"
                  theme="icon tertiary">
                  <vaadin-icon .svg="${ne.close}"></vaadin-icon>
                  <vaadin-tooltip slot="tooltip" text="Close"></vaadin-tooltip>
                </vaadin-button>
              `,
        [this.panelHeaderUpdated]
      )}
            ${Mt(
        () => x`<div class="w-full h-full">${Gt(`<${e.tag}></${e.tag}>`)}</div>`,
        []
      )}
            ${e.footerActionsTag ? Vt(
        () => x`<div>
                      ${Gt(`<${e.footerActionsTag}></${e.footerActionsTag}>`)}
                    </div>`,
        []
      ) : me}></vaadin-dialog>`
    )}
    `;
  }
  hasDropTarget(e) {
    const t = e.children;
    for (const r of t) {
      const n = la(r.shadowRoot ?? r, "copilot-image-upload");
      if (n && getComputedStyle(n).display !== "none")
        return !0;
    }
    return !1;
  }
  /**
   * Queries open panel dialogs. This method returns sorted list of dialogs based on their stack orders
   */
  getOpenPanelContainerDialogs() {
    const e = Array.from(this.querySelectorAll("vaadin-dialog[panel-container][opened]")), t = N.panelStackingOrder;
    return e.sort((r, n) => {
      const i = r.dataset.panelTag, a = n.dataset.panelTag;
      return i === null ? a === null ? 0 : 1 : a === null ? -1 : t.indexOf(i) - t.indexOf(a);
    }), e;
  }
  getDialogByPanelTag(e) {
    return e ? this.querySelector(`vaadin-dialog#${e}-dialog`) : null;
  }
  getToolbar() {
    const e = document.querySelector("copilot-main");
    return e ? e.shadowRoot.querySelector("copilot-toolbar") : null;
  }
  /**
   * Adjusts panel position to keep it within the viewport by applying edge offsets.
   *
   * The method preserves the given width and height and only shifts `top`/`left`:
   * - If left/top are negative, it moves the panel back to 0 on that axis.
   * - If right/bottom exceed viewport bounds minus padding, it moves the panel inward by the overflow amount with additional padding
   *
   * No left/top custom padding, min-size, or max-size constraints are applied.
   */
  getViewportAdjustedPosition(e) {
    const t = this.getViewportPaddingPx(), r = Number.isFinite(e.width) ? e.width : 0, n = Number.isFinite(e.height) ? e.height : 0;
    let i = Number.isFinite(e.top) ? e.top : 0, a = Number.isFinite(e.left) ? e.left : 0;
    const o = a + r, d = i + n;
    let c = 0, s = 0;
    return a < 0 ? c = -a : o > window.innerWidth - t && (c = window.innerWidth - t - o), i < 0 ? s = -i : d > window.innerHeight - t && (s = window.innerHeight - t - d), a += c, i += s, { top: i, left: a, width: r, height: n };
  }
  getViewportPaddingPx() {
    const e = Number.parseFloat(getComputedStyle(this).fontSize);
    return Number.isFinite(e) ? e : 16;
  }
};
ln([
  ve()
], St.prototype, "panelHeaderUpdated", 2);
ln([
  ve()
], St.prototype, "openedPanelConfigurationsSorted", 2);
St = ln([
  Ne("copilot-panel-manager")
], St);
const Yr = window.Vaadin.copilot.customComponentHandler;
if (!Yr)
  throw new Error("Tried to access custom component handler before it was initialized.");
function Fa(e) {
  F.setOperationWaitsHmrUpdate(e, 3e4);
}
X.on("undoRedo", (e) => {
  const r = { files: ka(e), uiId: ca() }, n = e.detail.undo ? "copilot-plugin-undo" : "copilot-plugin-redo", i = e.detail.undo ? "undo" : "redo";
  an(i), Fa(Ci.RedoUndo), xi(n, r, (a) => {
    if (!a.data.performed) {
      if (a.data.error && a.data.error.message) {
        _e({
          type: oe.ERROR,
          message: a.data.error.message
        }), X.emit("vite-after-update", {});
        return;
      }
      _e({
        type: oe.INFORMATION,
        message: `Nothing to ${i}`
      }), X.emit("vite-after-update", {});
    }
  });
});
function ka(e) {
  if (e.detail.files)
    return e.detail.files;
  const t = Yr.getActiveDrillDownContext();
  if (t) {
    const r = Yr.getCustomComponentInfo(t);
    if (r)
      return new Array(r.customComponentFilePath);
  }
  return da();
}
var ja = Object.getOwnPropertyDescriptor, Za = (e, t, r, n) => {
  for (var i = n > 1 ? void 0 : n ? ja(t, r) : t, a = e.length - 1, o; a >= 0; a--)
    (o = e[a]) && (i = o(i) || i);
  return i;
};
let $n = class extends _t {
  createRenderRoot() {
    return this;
  }
  connectedCallback() {
    super.connectedCallback(), this.classList.add("end-2", "fixed", "top-2", "z-100"), X.on("show-notification", (e) => {
      const t = e.detail.notification;
      _e(t);
    });
  }
  /**
   * Notifications with interactive elements are problematic; we should consider alternatives (i.e. alertdialog).
   */
  render() {
    return x`<section aria-label="Notifications" aria-live="polite" class="flex flex-col-reverse list-none m-0 p-0">
      ${et(
      F.notifications,
      (e) => e.id,
      (e) => this.renderNotification(e)
    )}
    </section>`;
  }
  renderNotification(e) {
    return x`
      <div
        class="duration-300 origin-top transition-all ${e.animatingIn ? "delay-300 max-h-0 opacity-0 scale-99 -translate-y-2" : e.animatingOut ? "delay-0 max-h-0 opacity-0 scale-99 -translate-y-2" : "max-h-screen"}"
        data-testid="message"
        ?data-error="${e.type === oe.ERROR}">
        <div class="bg-gray-1 dark:bg-gray-5 border flex gap-2 mb-2 p-3 relative rounded-md shadow-xl w-sm">
          <vaadin-icon
            class="${e.type === oe.ERROR ? "text-ruby-11" : e.type === oe.WARNING ? "text-amber-11" : "text-blue-11"}"
            .svg="${e.type === oe.WARNING || e.type === oe.ERROR ? ne.warning : ne.info}"></vaadin-icon>
          <div class="flex flex-col flex-1">
            <h2 class="m-0 text-sm">${e.message}</h2>
            <div
              class="break-word flex flex-col items-start text-secondary text-xs"
              ?hidden="${!e.details && !e.link}">
              ${ua(e.details)}
              ${e.link ? x`<a class="ahreflike" href="${e.link}" target="_blank">Learn more</a>` : ""}
            </div>
            ${e.dismissId ? x` <hr class="border-b border-e-0 border-s-0 border-t-0 mx-0 my-3 w-full" />
                  <vaadin-checkbox
                    ?checked=${e.dontShowAgain}
                    @change=${() => this.toggleDontShowAgain(e)}
                    label="${Ua(e)}">
                  </vaadin-checkbox>` : ""}
          </div>
          <vaadin-button
            aria-label="Close"
            class="absolute end-1.5 top-1.5"
            @click=${(t) => {
      Di(e), t.stopPropagation();
    }}
            id="dismiss"
            theme="icon tertiary">
            <vaadin-icon .svg="${ne.close}"></vaadin-icon>
            <vaadin-tooltip slot="tooltip" text="Close"></vaadin-tooltip>
          </vaadin-button>
        </div>
      </div>
    `;
  }
  toggleDontShowAgain(e) {
    e.dontShowAgain = !e.dontShowAgain, this.requestUpdate();
  }
};
$n = Za([
  Ne("copilot-notifications-container")
], $n);
function Ua(e) {
  return e.dontShowAgainMessage ? e.dontShowAgainMessage : "Don't show again";
}
_e({
  type: oe.WARNING,
  message: "Development Mode",
  details: "This application is running in development mode.",
  dismissId: "devmode"
});
const zr = ha(async () => {
  await fa();
}, 100);
X.on("vite-after-update", () => {
  Pt().default || zr();
});
function Li() {
  Pt().default || (zr.clear(), zr(), ga());
}
if (window.__REACT_DEVTOOLS_GLOBAL_HOOK__) {
  const e = window.__REACT_DEVTOOLS_GLOBAL_HOOK__, t = e.onCommitFiberRoot;
  e.onCommitFiberRoot = (r, n, i, a) => (Li(), t(r, n, i, a));
}
pa(() => {
  const e = window?.Vaadin?.connectionState;
  if (e)
    return e;
}).then((e) => {
  e.addStateChangeListener && typeof e.addStateChangeListener == "function" ? e.addStateChangeListener((t, r) => {
    t === "loading" && r === "connected" && !Pt().default && Li();
  }) : console.error("Unable to add listener for connection state changes");
});
X.on("copilot-plugin-state", (e) => {
  F.setIdePluginState(e.detail), e.preventDefault();
});
X.on("copilot-early-project-state", (e) => {
  K.setSpringSecurityEnabled(e.detail.springSecurityEnabled), K.setSpringJpaDataEnabled(e.detail.springJpaDataEnabled), K.setSpringJpaDatasourceInitialization(e.detail.springJpaDatasourceInitialization), K.setSupportsHilla(e.detail.supportsHilla), K.setSpringApplication(e.detail.springApplication), K.setUrlPrefix(e.detail.urlPrefix), K.setServerVersions(e.detail.serverVersions), K.setJdkInfo(e.detail.jdkInfo), on() === "success" && an("hotswap-active", { value: ma() }), e.preventDefault();
});
X.on("copilot-ide-notification", (e) => {
  _e({
    type: oe[e.detail.type],
    message: e.detail.message,
    dismissId: e.detail.dismissId
  }), e.preventDefault();
});
var Ba = Object.defineProperty, Ga = Object.getOwnPropertyDescriptor, Mi = (e, t, r, n) => {
  for (var i = n > 1 ? void 0 : n ? Ga(t, r) : t, a = e.length - 1, o; a >= 0; a--)
    (o = e[a]) && (i = (n ? o(t, r, i) : o(i)) || i);
  return n && i && Ba(t, r, i), i;
};
let Jr = class extends Nt {
  constructor() {
    super(...arguments), this.rememberChoice = !1, this.opened = !1, this.handleESC = (e) => {
      Pt().appInteractable || !this.opened || (e.key === "Escape" && this.sendEvent("cancel"), e.preventDefault(), e.stopPropagation());
    };
  }
  createRenderRoot() {
    return this;
  }
  connectedCallback() {
    super.connectedCallback(), this.addESCListener();
  }
  disconnectedCallback() {
    super.disconnectedCallback(), this.removeESCListener();
  }
  render() {
    return x` <vaadin-dialog
      id="ai-dialog"
      no-close-on-outside-click
      class="ai-dialog"
      ?opened=${this.opened}
      ${Ht(
      () => x`
          <h2>This Operation Uses AI</h2>
          <vaadin-icon .svg="${ne.sparkles}"></vaadin-icon>
        `
    )}
      ${Mt(
      () => x`
          <p>AI is a third-party service that will receive some of your project code as context for the operation.</p>
          <label>
            <input
              id="ai-dialog-checkbox"
              type="checkbox"
              @change=${(e) => {
        this.rememberChoice = e.target.checked;
      }} />Don’t ask again
          </label>
        `
    )}
      ${Vt(
      () => x`
          <button @click=${() => this.sendEvent("cancel")}>Cancel</button>
          <button class="primary" @click=${() => this.sendEvent("ok")}>OK</button>
        `
    )}></vaadin-dialog>`;
  }
  sendEvent(e) {
    this.dispatchEvent(
      new CustomEvent("ai-usage-response", {
        detail: { response: e, rememberChoice: this.rememberChoice }
      })
    );
  }
  addESCListener() {
    document.addEventListener("keydown", this.handleESC, { capture: !0 });
  }
  removeESCListener() {
    document.removeEventListener("keydown", this.handleESC, { capture: !0 });
  }
};
Mi([
  Ae()
], Jr.prototype, "opened", 2);
Jr = Mi([
  Ne("copilot-ai-usage-confirmation-dialog")
], Jr);
var Xa = Object.defineProperty, Wa = Object.getOwnPropertyDescriptor, qe = (e, t, r, n) => {
  for (var i = n > 1 ? void 0 : n ? Wa(t, r) : t, a = e.length - 1, o; a >= 0; a--)
    (o = e[a]) && (i = (n ? o(t, r, i) : o(i)) || i);
  return n && i && Xa(t, r, i), i;
};
const In = {
  info: "UI state info",
  stacktrace: "Exception details",
  versions: "Vaadin, Java, OS, etc.."
};
let Pe = class extends Nt {
  constructor() {
    super(...arguments), this.exceptionReport = void 0, this.dialogOpened = !1, this.visibleItemIndex = 0, this.versions = void 0, this.selectedItems = [], this.eventListener = (e) => {
      this.exceptionReport = e.detail, this.selectedItems = this.exceptionReport.items.map((t, r) => r), this.visibleItemIndex = 0, this.searchInputValue = void 0, this.dialogOpened = this.exceptionReport !== void 0;
    };
  }
  connectedCallback() {
    super.connectedCallback(), X.on("submit-exception-report-clicked", this.eventListener);
  }
  createRenderRoot() {
    return this;
  }
  disconnectedCallback() {
    super.disconnectedCallback(), X.off("submit-exception-report-clicked", this.eventListener);
  }
  close() {
    this.dialogOpened = !1;
  }
  clear() {
    this.exceptionReport = void 0;
  }
  render() {
    let e = "";
    return this.exceptionReport && this.exceptionReport.items.length > 0 && (e = this.exceptionReport.items[this.visibleItemIndex].content), x` <vaadin-dialog
      id="report-exception-dialog"
      no-close-on-outside-click
      draggable
      ?opened=${this.dialogOpened}
      @closed="${() => {
      this.clear();
    }}"
      @opened-changed="${(t) => {
      t.detail.value || this.close();
    }}"
      ${Ht(
      () => x`
          <div
            class="draggable"
            style="display: flex; justify-content: space-between; align-items: center; width: 100%">
            <h2>Send report</h2>
            <vaadin-button theme="tertiary" @click="${this.close}">
              <vaadin-icon icon="lumo:cross"></vaadin-icon>
            </vaadin-button>
          </div>
        `
    )}
      ${Mt(
      () => x`
          <div class="description-container">
            <vaadin-text-area
              @input=${(t) => {
        this.searchInputValue = t.target.value;
      }}
              label="Description of the Bug"
              placeholder="A short, concise description of the bug and why you consider it a bug."></vaadin-text-area>
          </div>
          <div class="list-preview-container">
            <div class="left-menu">
              <div class="section-title">Include in Report</div>
              <vaadin-list-box
                single
                selected="${this.visibleItemIndex}"
                @selected-changed="${(t) => {
        this.visibleItemIndex = t.detail.value;
      }}">
                ${this.exceptionReport?.items.map(
        (t, r) => x` <vaadin-item>
                      <input
                        type="checkbox"
                        .checked="${this.selectedItems.indexOf(r) !== -1}"
                        @change="${(n) => {
          const i = n.target, a = [...this.selectedItems];
          if (i.checked)
            a.push(r), this.selectedItems = [...this.selectedItems];
          else {
            const o = this.selectedItems.indexOf(r);
            a.splice(o, 1);
          }
          this.selectedItems = a;
        }}" />
                      <div class="item-content">
                        <span class="item-name"> ${t.name} </span>
                        <span class="item-description">${this.renderItemDescription(t)}</span>
                      </div>
                    </vaadin-item>`
      )}
              </vaadin-list-box>
            </div>
            <div class="right-menu">
              <div class="section-title">Preview: ${this.exceptionReport?.items[this.visibleItemIndex].name}</div>
              <code class="codeblock">${e}</code>
            </div>
          </div>
        `,
      [this.exceptionReport, this.visibleItemIndex, this.selectedItems]
    )}
      ${Vt(
      () => x`
          <button
            id="cancel"
            @click=${() => {
        this.close();
      }}>
            Cancel
          </button>

          <button
            id="submit"
            class="primary"
            @click=${() => {
        this.submitErrorToGithub(), this.close();
      }}>
            Submit in GitHub
            <vaadin-tooltip
              for="submit"
              text="${this.bodyLengthExceeds() ? "The error report will be copied to clipboard and blank new issue page will be opened" : "New issue page will be opened with data loaded"}"
              position="top-start"></vaadin-tooltip>
          </button>
        `,
      [this.exceptionReport, this.selectedItems, this.searchInputValue]
    )}></vaadin-dialog>`;
  }
  renderItemDescription(e) {
    return Object.keys(In).indexOf(e.name.toLowerCase()) !== -1 ? In[e.name.toLowerCase()] : null;
  }
  bodyLengthExceeds() {
    const e = this.getIssueBodyNotEncoded();
    return e !== void 0 && encodeURIComponent(e).length > 7500;
  }
  getIssueBodyNotEncoded() {
    if (!this.exceptionReport)
      return;
    const e = this.exceptionReport.items.filter((t, r) => this.selectedItems.indexOf(r) !== -1).map((t) => {
      let r = "```";
      return t.name.includes(".java") && (r = `${r}java`), `## ${t.name} 
 
 ${r}
${t.content}
\`\`\``;
    });
    return this.searchInputValue ? `## Description of the bug 
 ${this.searchInputValue} 
 ${e.join(`
`)}` : `## Description of the bug 
 Please enter bug description here 
 ${e.join(`
`)}`;
  }
  submitErrorToGithub() {
    const e = this.exceptionReport;
    if (!e)
      return;
    const t = encodeURIComponent(e.title ?? "Bug report "), r = this.getIssueBodyNotEncoded();
    if (!r)
      return;
    let n = encodeURIComponent(r);
    n.length >= 7500 && (_i(r), n = encodeURIComponent("Please paste report here. It was automatically added to your clipboard."));
    const i = `https://github.com/vaadin/copilot/issues/new?title=${t}&body=${n}`;
    window.open(i, "_blank");
  }
};
qe([
  ve()
], Pe.prototype, "exceptionReport", 2);
qe([
  ve()
], Pe.prototype, "dialogOpened", 2);
qe([
  ve()
], Pe.prototype, "visibleItemIndex", 2);
qe([
  ve()
], Pe.prototype, "versions", 2);
qe([
  ve()
], Pe.prototype, "selectedItems", 2);
qe([
  ve()
], Pe.prototype, "searchInputValue", 2);
Pe = qe([
  Ne("copilot-report-exception-dialog")
], Pe);
let ut;
X.on("copilot-project-compilation-error", (e) => {
  if (e.detail.error) {
    let t;
    if (e.detail.files && e.detail.files.length > 0) {
      const r = F.idePluginState?.supportedActions?.includes("undo") ? x`
            <vaadin-button
              @click="${(n) => {
        n.preventDefault(), X.emit("undoRedo", { undo: !0, files: e.detail.files?.map((i) => i.path) });
      }}"
              theme="primary">
              <vaadin-icon slot="prefix" .svg="${ne.undo}"></vaadin-icon>
              Undo Last Change
            </vaadin-button>
          ` : me;
      t = Lt(x`
        <span> Following files have compilation errors: </span>
        <ul class="list-none mb-0 mt-2 p-0">
          ${e.detail.files.map(
        (n) => x` <li>
                <vaadin-button
                  @click="${() => {
          X.emit("show-in-ide", { javaSource: { absoluteFilePath: n.path } });
        }}"
                  theme="tertiary">
                  <vaadin-icon slot="prefix" .svg="${ne.code}"></vaadin-icon>
                  ${n.name}
                </vaadin-button>
              </li>`
      )}
        </ul>
        <hr class="border-b border-e-0 border-s-0 border-t-0 mb-3 mt-2 mx-0 w-full" />
        ${r}
      `);
    } else
      t = "Project contains one or more compilation errors.";
    ut = _e({
      message: "Compilation error",
      details: t,
      type: oe.WARNING,
      delay: 3e4
    });
  } else
    ut && Di(ut), ut = void 0;
});
var Ya = Object.defineProperty, za = Object.getOwnPropertyDescriptor, Hi = (e, t, r, n) => {
  for (var i = n > 1 ? void 0 : n ? za(t, r) : t, a = e.length - 1, o; a >= 0; a--)
    (o = e[a]) && (i = (n ? o(t, r, i) : o(i)) || i);
  return n && i && Ya(t, r, i), i;
};
let Kr = class extends Nt {
  constructor() {
    super(...arguments), this.text = () => (this.parentElement.textContent ?? "").trim();
  }
  createRenderRoot() {
    return this;
  }
  render() {
    return x`<vaadin-button
      aria-label="Copy to clipboard"
      @click=${(e) => {
      e.stopPropagation(), e.preventDefault();
      const t = this.text();
      _i(t);
    }}
      theme="icon tertiary">
      <vaadin-icon .svg="${ne.fileCopy}"></vaadin-icon>
      <vaadin-tooltip slot="tooltip" text="Copy to clipboard"></vaadin-tooltip>
    </vaadin-button> `;
  }
};
Hi([
  Ae({ type: Function })
], Kr.prototype, "text", 2);
Kr = Hi([
  Ne("copilot-copy")
], Kr);
var Ja = {
  202: "Accepted",
  502: "Bad Gateway",
  400: "Bad Request",
  409: "Conflict",
  100: "Continue",
  201: "Created",
  417: "Expectation Failed",
  424: "Failed Dependency",
  403: "Forbidden",
  504: "Gateway Timeout",
  410: "Gone",
  505: "HTTP Version Not Supported",
  418: "I'm a teapot",
  419: "Insufficient Space on Resource",
  507: "Insufficient Storage",
  500: "Internal Server Error",
  411: "Length Required",
  423: "Locked",
  420: "Method Failure",
  405: "Method Not Allowed",
  301: "Moved Permanently",
  302: "Moved Temporarily",
  207: "Multi-Status",
  300: "Multiple Choices",
  511: "Network Authentication Required",
  204: "No Content",
  203: "Non Authoritative Information",
  406: "Not Acceptable",
  404: "Not Found",
  501: "Not Implemented",
  304: "Not Modified",
  200: "OK",
  206: "Partial Content",
  402: "Payment Required",
  308: "Permanent Redirect",
  412: "Precondition Failed",
  428: "Precondition Required",
  102: "Processing",
  103: "Early Hints",
  426: "Upgrade Required",
  407: "Proxy Authentication Required",
  431: "Request Header Fields Too Large",
  408: "Request Timeout",
  413: "Request Entity Too Large",
  414: "Request-URI Too Long",
  416: "Requested Range Not Satisfiable",
  205: "Reset Content",
  303: "See Other",
  503: "Service Unavailable",
  101: "Switching Protocols",
  307: "Temporary Redirect",
  429: "Too Many Requests",
  401: "Unauthorized",
  451: "Unavailable For Legal Reasons",
  422: "Unprocessable Entity",
  415: "Unsupported Media Type",
  305: "Use Proxy",
  421: "Misdirected Request"
};
function Ka(e) {
  var t = Ja[e.toString()];
  if (!t)
    throw new Error("Status code does not exist: " + e);
  return t;
}
function Vi(e) {
  return `endpoint-request-${e.id}`;
}
X.on("endpoint-request", (e) => {
  const t = e.detail, r = Vi(t);
  delete t.id;
  const n = Object.values(t.params), i = n.map(Tt).join(", ");
  X.emit("log", {
    id: r,
    type: oe.INFORMATION,
    message: `Called endpoint ${t.endpoint}.${t.method}(${i})`,
    expandedMessage: Lt(
      x`Called endpoint ${t.endpoint}.${t.method} with parameters
        <code class="codeblock"><copilot-copy></copilot-copy>${Tt(n)}</code>`
    ),
    details: "Response: <pending>"
  });
});
X.on("endpoint-response", (e) => {
  let t;
  try {
    t = JSON.parse(e.detail.text);
  } catch {
    t = e.detail.text;
  }
  const r = {}, n = e.detail.status ?? 200;
  n === 200 ? (r.details = `Response: ${Tt(t)}`, r.expandedDetails = Lt(
    x`Response: <code class="codeblock"><copilot-copy></copilot-copy>${Tt(t)}</code>`
  )) : (r.details = `Error: ${n} ${Ka(n)}`, r.type = oe.ERROR), X.emit("update-log", {
    id: Vi(e.detail),
    ...r
  });
});
function Tt(e) {
  return typeof e == "string" ? `${e}` : JSON.stringify(e, void 0, 2);
}
var Qa = Object.defineProperty, eo = Object.getOwnPropertyDescriptor, Le = (e, t, r, n) => {
  for (var i = n > 1 ? void 0 : n ? eo(t, r) : t, a = e.length - 1, o; a >= 0; a--)
    (o = e[a]) && (i = (n ? o(t, r, i) : o(i)) || i);
  return n && i && Qa(t, r, i), i;
};
class to extends CustomEvent {
  constructor(t) {
    super("show-in-ide-clicked", {
      detail: t,
      bubbles: !0,
      composed: !0
    });
  }
}
let ge = class extends Nt {
  constructor() {
    super(...arguments), this.iconHidden = !1, this.linkHidden = !1, this.tooltipText = void 0, this.linkText = void 0, this.source = void 0, this.javaSource = void 0, this.node = void 0;
  }
  static get styles() {
    return [bn(va), bn(Ea)];
  }
  render() {
    if (this.iconHidden) {
      if (!this.linkHidden)
        return this.renderContent(this.renderAnchor());
    } else return this.linkHidden ? this.renderContent(this.renderIcon()) : this.renderContent([this.renderIcon(), this.renderAnchor()]);
    return me;
  }
  renderContent(e) {
    return x` <div class="contents">${e}</div> `;
  }
  renderIcon() {
    const e = this.tooltipText ?? `Open ${this.getFileName()} in IDE`;
    return x`
      <vaadin-button
        aria-label="${e}"
        id="show-in-ide"
        theme="icon tertiary"
        @click=${(t) => {
      t.stopPropagation(), t.preventDefault(), this._showInIde();
    }}>
        <vaadin-icon .svg="${ne.code}"></vaadin-icon>
        <vaadin-tooltip slot="tooltip" text="${e}"></vaadin-tooltip>
      </vaadin-button>
    `;
  }
  renderAnchor() {
    return x`
      <a
        class="text-blue-11"
        href="#"
        id="link"
        @click=${(e) => {
      e.preventDefault(), this._showInIde();
    }}
        >${this.linkText ?? this.getFileName() ?? ""}</a
      >
      ${this.renderTooltip("link")}
    `;
  }
  dispatchClickedEvent() {
    this.dispatchEvent(
      new to({
        source: this.source,
        javaSource: this.javaSource,
        node: this.node
      })
    );
  }
  renderTooltip(e) {
    const t = this.tooltipText ?? `Open ${this.getFileName()} in IDE`;
    return x`<vaadin-tooltip for="${e}" text="${t}" position="top-start"></vaadin-tooltip>`;
  }
  getFileName() {
    if (this.tooltipText)
      return this.tooltipText;
    if (this.source && this.source.fileName)
      return ba(this.source.fileName);
    if (this.javaSource)
      return this.javaSource.className;
  }
  _showInIde() {
    X.emit("show-in-ide", {
      source: this.source,
      javaSource: this.javaSource,
      node: this.node
    }), this.dispatchClickedEvent();
  }
};
ge.TAG = "copilot-go-to-source";
Le([
  Ae({ type: Boolean })
], ge.prototype, "iconHidden", 2);
Le([
  Ae({ type: Boolean })
], ge.prototype, "linkHidden", 2);
Le([
  Ae()
], ge.prototype, "tooltipText", 2);
Le([
  Ae()
], ge.prototype, "linkText", 2);
Le([
  Ae()
], ge.prototype, "source", 2);
Le([
  Ae()
], ge.prototype, "javaSource", 2);
Le([
  Ae()
], ge.prototype, "node", 2);
ge = Le([
  Ne(ge.TAG)
], ge);
/**!
 * Sortable 1.15.6
 * @author	RubaXa   <trash@rubaxa.org>
 * @author	owenm    <owen23355@gmail.com>
 * @license MIT
 */
function Sn(e, t) {
  var r = Object.keys(e);
  if (Object.getOwnPropertySymbols) {
    var n = Object.getOwnPropertySymbols(e);
    t && (n = n.filter(function(i) {
      return Object.getOwnPropertyDescriptor(e, i).enumerable;
    })), r.push.apply(r, n);
  }
  return r;
}
function ye(e) {
  for (var t = 1; t < arguments.length; t++) {
    var r = arguments[t] != null ? arguments[t] : {};
    t % 2 ? Sn(Object(r), !0).forEach(function(n) {
      ro(e, n, r[n]);
    }) : Object.getOwnPropertyDescriptors ? Object.defineProperties(e, Object.getOwnPropertyDescriptors(r)) : Sn(Object(r)).forEach(function(n) {
      Object.defineProperty(e, n, Object.getOwnPropertyDescriptor(r, n));
    });
  }
  return e;
}
function vt(e) {
  "@babel/helpers - typeof";
  return typeof Symbol == "function" && typeof Symbol.iterator == "symbol" ? vt = function(t) {
    return typeof t;
  } : vt = function(t) {
    return t && typeof Symbol == "function" && t.constructor === Symbol && t !== Symbol.prototype ? "symbol" : typeof t;
  }, vt(e);
}
function ro(e, t, r) {
  return t in e ? Object.defineProperty(e, t, {
    value: r,
    enumerable: !0,
    configurable: !0,
    writable: !0
  }) : e[t] = r, e;
}
function Te() {
  return Te = Object.assign || function(e) {
    for (var t = 1; t < arguments.length; t++) {
      var r = arguments[t];
      for (var n in r)
        Object.prototype.hasOwnProperty.call(r, n) && (e[n] = r[n]);
    }
    return e;
  }, Te.apply(this, arguments);
}
function no(e, t) {
  if (e == null) return {};
  var r = {}, n = Object.keys(e), i, a;
  for (a = 0; a < n.length; a++)
    i = n[a], !(t.indexOf(i) >= 0) && (r[i] = e[i]);
  return r;
}
function io(e, t) {
  if (e == null) return {};
  var r = no(e, t), n, i;
  if (Object.getOwnPropertySymbols) {
    var a = Object.getOwnPropertySymbols(e);
    for (i = 0; i < a.length; i++)
      n = a[i], !(t.indexOf(n) >= 0) && Object.prototype.propertyIsEnumerable.call(e, n) && (r[n] = e[n]);
  }
  return r;
}
var ao = "1.15.6";
function Se(e) {
  if (typeof window < "u" && window.navigator)
    return !!/* @__PURE__ */ navigator.userAgent.match(e);
}
var Ce = Se(/(?:Trident.*rv[ :]?11\.|msie|iemobile|Windows Phone)/i), lt = Se(/Edge/i), Tn = Se(/firefox/i), rt = Se(/safari/i) && !Se(/chrome/i) && !Se(/android/i), cn = Se(/iP(ad|od|hone)/i), qi = Se(/chrome/i) && Se(/android/i), Fi = {
  capture: !1,
  passive: !1
};
function M(e, t, r) {
  e.addEventListener(t, r, !Ce && Fi);
}
function P(e, t, r) {
  e.removeEventListener(t, r, !Ce && Fi);
}
function At(e, t) {
  if (t) {
    if (t[0] === ">" && (t = t.substring(1)), e)
      try {
        if (e.matches)
          return e.matches(t);
        if (e.msMatchesSelector)
          return e.msMatchesSelector(t);
        if (e.webkitMatchesSelector)
          return e.webkitMatchesSelector(t);
      } catch {
        return !1;
      }
    return !1;
  }
}
function ki(e) {
  return e.host && e !== document && e.host.nodeType ? e.host : e.parentNode;
}
function pe(e, t, r, n) {
  if (e) {
    r = r || document;
    do {
      if (t != null && (t[0] === ">" ? e.parentNode === r && At(e, t) : At(e, t)) || n && e === r)
        return e;
      if (e === r) break;
    } while (e = ki(e));
  }
  return null;
}
var An = /\s+/g;
function ce(e, t, r) {
  if (e && t)
    if (e.classList)
      e.classList[r ? "add" : "remove"](t);
    else {
      var n = (" " + e.className + " ").replace(An, " ").replace(" " + t + " ", " ");
      e.className = (n + (r ? " " + t : "")).replace(An, " ");
    }
}
function I(e, t, r) {
  var n = e && e.style;
  if (n) {
    if (r === void 0)
      return document.defaultView && document.defaultView.getComputedStyle ? r = document.defaultView.getComputedStyle(e, "") : e.currentStyle && (r = e.currentStyle), t === void 0 ? r : r[t];
    !(t in n) && t.indexOf("webkit") === -1 && (t = "-webkit-" + t), n[t] = r + (typeof r == "string" ? "" : "px");
  }
}
function Be(e, t) {
  var r = "";
  if (typeof e == "string")
    r = e;
  else
    do {
      var n = I(e, "transform");
      n && n !== "none" && (r = n + " " + r);
    } while (!t && (e = e.parentNode));
  var i = window.DOMMatrix || window.WebKitCSSMatrix || window.CSSMatrix || window.MSCSSMatrix;
  return i && new i(r);
}
function ji(e, t, r) {
  if (e) {
    var n = e.getElementsByTagName(t), i = 0, a = n.length;
    if (r)
      for (; i < a; i++)
        r(n[i], i);
    return n;
  }
  return [];
}
function we() {
  var e = document.scrollingElement;
  return e || document.documentElement;
}
function z(e, t, r, n, i) {
  if (!(!e.getBoundingClientRect && e !== window)) {
    var a, o, d, c, s, l, u;
    if (e !== window && e.parentNode && e !== we() ? (a = e.getBoundingClientRect(), o = a.top, d = a.left, c = a.bottom, s = a.right, l = a.height, u = a.width) : (o = 0, d = 0, c = window.innerHeight, s = window.innerWidth, l = window.innerHeight, u = window.innerWidth), (t || r) && e !== window && (i = i || e.parentNode, !Ce))
      do
        if (i && i.getBoundingClientRect && (I(i, "transform") !== "none" || r && I(i, "position") !== "static")) {
          var h = i.getBoundingClientRect();
          o -= h.top + parseInt(I(i, "border-top-width")), d -= h.left + parseInt(I(i, "border-left-width")), c = o + a.height, s = d + a.width;
          break;
        }
      while (i = i.parentNode);
    if (n && e !== window) {
      var p = Be(i || e), E = p && p.a, f = p && p.d;
      p && (o /= f, d /= E, u /= E, l /= f, c = o + l, s = d + u);
    }
    return {
      top: o,
      left: d,
      bottom: c,
      right: s,
      width: u,
      height: l
    };
  }
}
function Cn(e, t, r) {
  for (var n = Oe(e, !0), i = z(e)[t]; n; ) {
    var a = z(n)[r], o = void 0;
    if (o = i >= a, !o) return n;
    if (n === we()) break;
    n = Oe(n, !1);
  }
  return !1;
}
function Ge(e, t, r, n) {
  for (var i = 0, a = 0, o = e.children; a < o.length; ) {
    if (o[a].style.display !== "none" && o[a] !== S.ghost && (n || o[a] !== S.dragged) && pe(o[a], r.draggable, e, !1)) {
      if (i === t)
        return o[a];
      i++;
    }
    a++;
  }
  return null;
}
function dn(e, t) {
  for (var r = e.lastElementChild; r && (r === S.ghost || I(r, "display") === "none" || t && !At(r, t)); )
    r = r.previousElementSibling;
  return r || null;
}
function he(e, t) {
  var r = 0;
  if (!e || !e.parentNode)
    return -1;
  for (; e = e.previousElementSibling; )
    e.nodeName.toUpperCase() !== "TEMPLATE" && e !== S.clone && (!t || At(e, t)) && r++;
  return r;
}
function xn(e) {
  var t = 0, r = 0, n = we();
  if (e)
    do {
      var i = Be(e), a = i.a, o = i.d;
      t += e.scrollLeft * a, r += e.scrollTop * o;
    } while (e !== n && (e = e.parentNode));
  return [t, r];
}
function oo(e, t) {
  for (var r in e)
    if (e.hasOwnProperty(r)) {
      for (var n in t)
        if (t.hasOwnProperty(n) && t[n] === e[r][n]) return Number(r);
    }
  return -1;
}
function Oe(e, t) {
  if (!e || !e.getBoundingClientRect) return we();
  var r = e, n = !1;
  do
    if (r.clientWidth < r.scrollWidth || r.clientHeight < r.scrollHeight) {
      var i = I(r);
      if (r.clientWidth < r.scrollWidth && (i.overflowX == "auto" || i.overflowX == "scroll") || r.clientHeight < r.scrollHeight && (i.overflowY == "auto" || i.overflowY == "scroll")) {
        if (!r.getBoundingClientRect || r === document.body) return we();
        if (n || t) return r;
        n = !0;
      }
    }
  while (r = r.parentNode);
  return we();
}
function so(e, t) {
  if (e && t)
    for (var r in t)
      t.hasOwnProperty(r) && (e[r] = t[r]);
  return e;
}
function Yt(e, t) {
  return Math.round(e.top) === Math.round(t.top) && Math.round(e.left) === Math.round(t.left) && Math.round(e.height) === Math.round(t.height) && Math.round(e.width) === Math.round(t.width);
}
var nt;
function Zi(e, t) {
  return function() {
    if (!nt) {
      var r = arguments, n = this;
      r.length === 1 ? e.call(n, r[0]) : e.apply(n, r), nt = setTimeout(function() {
        nt = void 0;
      }, t);
    }
  };
}
function lo() {
  clearTimeout(nt), nt = void 0;
}
function Ui(e, t, r) {
  e.scrollLeft += t, e.scrollTop += r;
}
function Bi(e) {
  var t = window.Polymer, r = window.jQuery || window.Zepto;
  return t && t.dom ? t.dom(e).cloneNode(!0) : r ? r(e).clone(!0)[0] : e.cloneNode(!0);
}
function Gi(e, t, r) {
  var n = {};
  return Array.from(e.children).forEach(function(i) {
    var a, o, d, c;
    if (!(!pe(i, t.draggable, e, !1) || i.animated || i === r)) {
      var s = z(i);
      n.left = Math.min((a = n.left) !== null && a !== void 0 ? a : 1 / 0, s.left), n.top = Math.min((o = n.top) !== null && o !== void 0 ? o : 1 / 0, s.top), n.right = Math.max((d = n.right) !== null && d !== void 0 ? d : -1 / 0, s.right), n.bottom = Math.max((c = n.bottom) !== null && c !== void 0 ? c : -1 / 0, s.bottom);
    }
  }), n.width = n.right - n.left, n.height = n.bottom - n.top, n.x = n.left, n.y = n.top, n;
}
var se = "Sortable" + (/* @__PURE__ */ new Date()).getTime();
function co() {
  var e = [], t;
  return {
    captureAnimationState: function() {
      if (e = [], !!this.options.animation) {
        var n = [].slice.call(this.el.children);
        n.forEach(function(i) {
          if (!(I(i, "display") === "none" || i === S.ghost)) {
            e.push({
              target: i,
              rect: z(i)
            });
            var a = ye({}, e[e.length - 1].rect);
            if (i.thisAnimationDuration) {
              var o = Be(i, !0);
              o && (a.top -= o.f, a.left -= o.e);
            }
            i.fromRect = a;
          }
        });
      }
    },
    addAnimationState: function(n) {
      e.push(n);
    },
    removeAnimationState: function(n) {
      e.splice(oo(e, {
        target: n
      }), 1);
    },
    animateAll: function(n) {
      var i = this;
      if (!this.options.animation) {
        clearTimeout(t), typeof n == "function" && n();
        return;
      }
      var a = !1, o = 0;
      e.forEach(function(d) {
        var c = 0, s = d.target, l = s.fromRect, u = z(s), h = s.prevFromRect, p = s.prevToRect, E = d.rect, f = Be(s, !0);
        f && (u.top -= f.f, u.left -= f.e), s.toRect = u, s.thisAnimationDuration && Yt(h, u) && !Yt(l, u) && // Make sure animatingRect is on line between toRect & fromRect
        (E.top - u.top) / (E.left - u.left) === (l.top - u.top) / (l.left - u.left) && (c = ho(E, h, p, i.options)), Yt(u, l) || (s.prevFromRect = l, s.prevToRect = u, c || (c = i.options.animation), i.animate(s, E, u, c)), c && (a = !0, o = Math.max(o, c), clearTimeout(s.animationResetTimer), s.animationResetTimer = setTimeout(function() {
          s.animationTime = 0, s.prevFromRect = null, s.fromRect = null, s.prevToRect = null, s.thisAnimationDuration = null;
        }, c), s.thisAnimationDuration = c);
      }), clearTimeout(t), a ? t = setTimeout(function() {
        typeof n == "function" && n();
      }, o) : typeof n == "function" && n(), e = [];
    },
    animate: function(n, i, a, o) {
      if (o) {
        I(n, "transition", ""), I(n, "transform", "");
        var d = Be(this.el), c = d && d.a, s = d && d.d, l = (i.left - a.left) / (c || 1), u = (i.top - a.top) / (s || 1);
        n.animatingX = !!l, n.animatingY = !!u, I(n, "transform", "translate3d(" + l + "px," + u + "px,0)"), this.forRepaintDummy = uo(n), I(n, "transition", "transform " + o + "ms" + (this.options.easing ? " " + this.options.easing : "")), I(n, "transform", "translate3d(0,0,0)"), typeof n.animated == "number" && clearTimeout(n.animated), n.animated = setTimeout(function() {
          I(n, "transition", ""), I(n, "transform", ""), n.animated = !1, n.animatingX = !1, n.animatingY = !1;
        }, o);
      }
    }
  };
}
function uo(e) {
  return e.offsetWidth;
}
function ho(e, t, r, n) {
  return Math.sqrt(Math.pow(t.top - e.top, 2) + Math.pow(t.left - e.left, 2)) / Math.sqrt(Math.pow(t.top - r.top, 2) + Math.pow(t.left - r.left, 2)) * n.animation;
}
var Fe = [], zt = {
  initializeByDefault: !0
}, ct = {
  mount: function(t) {
    for (var r in zt)
      zt.hasOwnProperty(r) && !(r in t) && (t[r] = zt[r]);
    Fe.forEach(function(n) {
      if (n.pluginName === t.pluginName)
        throw "Sortable: Cannot mount plugin ".concat(t.pluginName, " more than once");
    }), Fe.push(t);
  },
  pluginEvent: function(t, r, n) {
    var i = this;
    this.eventCanceled = !1, n.cancel = function() {
      i.eventCanceled = !0;
    };
    var a = t + "Global";
    Fe.forEach(function(o) {
      r[o.pluginName] && (r[o.pluginName][a] && r[o.pluginName][a](ye({
        sortable: r
      }, n)), r.options[o.pluginName] && r[o.pluginName][t] && r[o.pluginName][t](ye({
        sortable: r
      }, n)));
    });
  },
  initializePlugins: function(t, r, n, i) {
    Fe.forEach(function(d) {
      var c = d.pluginName;
      if (!(!t.options[c] && !d.initializeByDefault)) {
        var s = new d(t, r, t.options);
        s.sortable = t, s.options = t.options, t[c] = s, Te(n, s.defaults);
      }
    });
    for (var a in t.options)
      if (t.options.hasOwnProperty(a)) {
        var o = this.modifyOption(t, a, t.options[a]);
        typeof o < "u" && (t.options[a] = o);
      }
  },
  getEventProperties: function(t, r) {
    var n = {};
    return Fe.forEach(function(i) {
      typeof i.eventProperties == "function" && Te(n, i.eventProperties.call(r[i.pluginName], t));
    }), n;
  },
  modifyOption: function(t, r, n) {
    var i;
    return Fe.forEach(function(a) {
      t[a.pluginName] && a.optionListeners && typeof a.optionListeners[r] == "function" && (i = a.optionListeners[r].call(t[a.pluginName], n));
    }), i;
  }
};
function fo(e) {
  var t = e.sortable, r = e.rootEl, n = e.name, i = e.targetEl, a = e.cloneEl, o = e.toEl, d = e.fromEl, c = e.oldIndex, s = e.newIndex, l = e.oldDraggableIndex, u = e.newDraggableIndex, h = e.originalEvent, p = e.putSortable, E = e.extraEventProperties;
  if (t = t || r && r[se], !!t) {
    var f, $ = t.options, _ = "on" + n.charAt(0).toUpperCase() + n.substr(1);
    window.CustomEvent && !Ce && !lt ? f = new CustomEvent(n, {
      bubbles: !0,
      cancelable: !0
    }) : (f = document.createEvent("Event"), f.initEvent(n, !0, !0)), f.to = o || r, f.from = d || r, f.item = i || r, f.clone = a, f.oldIndex = c, f.newIndex = s, f.oldDraggableIndex = l, f.newDraggableIndex = u, f.originalEvent = h, f.pullMode = p ? p.lastPutMode : void 0;
    var H = ye(ye({}, E), ct.getEventProperties(n, t));
    for (var D in H)
      f[D] = H[D];
    r && r.dispatchEvent(f), $[_] && $[_].call(t, f);
  }
}
var po = ["evt"], ae = function(t, r) {
  var n = arguments.length > 2 && arguments[2] !== void 0 ? arguments[2] : {}, i = n.evt, a = io(n, po);
  ct.pluginEvent.bind(S)(t, r, ye({
    dragEl: g,
    parentEl: G,
    ghostEl: A,
    rootEl: Z,
    nextEl: Ve,
    lastDownEl: Et,
    cloneEl: U,
    cloneHidden: De,
    dragStarted: Je,
    putSortable: Q,
    activeSortable: S.active,
    originalEvent: i,
    oldIndex: Ze,
    oldDraggableIndex: it,
    newIndex: de,
    newDraggableIndex: xe,
    hideGhostForTarget: zi,
    unhideGhostForTarget: Ji,
    cloneNowHidden: function() {
      De = !0;
    },
    cloneNowShown: function() {
      De = !1;
    },
    dispatchSortableEvent: function(d) {
      re({
        sortable: r,
        name: d,
        originalEvent: i
      });
    }
  }, a));
};
function re(e) {
  fo(ye({
    putSortable: Q,
    cloneEl: U,
    targetEl: g,
    rootEl: Z,
    oldIndex: Ze,
    oldDraggableIndex: it,
    newIndex: de,
    newDraggableIndex: xe
  }, e));
}
var g, G, A, Z, Ve, Et, U, De, Ze, de, it, xe, ht, Q, je = !1, Ct = !1, xt = [], Me, fe, Jt, Kt, Dn, On, Je, ke, at, ot = !1, ft = !1, bt, ee, Qt = [], Qr = !1, Dt = [], qt = typeof document < "u", pt = cn, _n = lt || Ce ? "cssFloat" : "float", mo = qt && !qi && !cn && "draggable" in document.createElement("div"), Xi = function() {
  if (qt) {
    if (Ce)
      return !1;
    var e = document.createElement("x");
    return e.style.cssText = "pointer-events:auto", e.style.pointerEvents === "auto";
  }
}(), Wi = function(t, r) {
  var n = I(t), i = parseInt(n.width) - parseInt(n.paddingLeft) - parseInt(n.paddingRight) - parseInt(n.borderLeftWidth) - parseInt(n.borderRightWidth), a = Ge(t, 0, r), o = Ge(t, 1, r), d = a && I(a), c = o && I(o), s = d && parseInt(d.marginLeft) + parseInt(d.marginRight) + z(a).width, l = c && parseInt(c.marginLeft) + parseInt(c.marginRight) + z(o).width;
  if (n.display === "flex")
    return n.flexDirection === "column" || n.flexDirection === "column-reverse" ? "vertical" : "horizontal";
  if (n.display === "grid")
    return n.gridTemplateColumns.split(" ").length <= 1 ? "vertical" : "horizontal";
  if (a && d.float && d.float !== "none") {
    var u = d.float === "left" ? "left" : "right";
    return o && (c.clear === "both" || c.clear === u) ? "vertical" : "horizontal";
  }
  return a && (d.display === "block" || d.display === "flex" || d.display === "table" || d.display === "grid" || s >= i && n[_n] === "none" || o && n[_n] === "none" && s + l > i) ? "vertical" : "horizontal";
}, go = function(t, r, n) {
  var i = n ? t.left : t.top, a = n ? t.right : t.bottom, o = n ? t.width : t.height, d = n ? r.left : r.top, c = n ? r.right : r.bottom, s = n ? r.width : r.height;
  return i === d || a === c || i + o / 2 === d + s / 2;
}, vo = function(t, r) {
  var n;
  return xt.some(function(i) {
    var a = i[se].options.emptyInsertThreshold;
    if (!(!a || dn(i))) {
      var o = z(i), d = t >= o.left - a && t <= o.right + a, c = r >= o.top - a && r <= o.bottom + a;
      if (d && c)
        return n = i;
    }
  }), n;
}, Yi = function(t) {
  function r(a, o) {
    return function(d, c, s, l) {
      var u = d.options.group.name && c.options.group.name && d.options.group.name === c.options.group.name;
      if (a == null && (o || u))
        return !0;
      if (a == null || a === !1)
        return !1;
      if (o && a === "clone")
        return a;
      if (typeof a == "function")
        return r(a(d, c, s, l), o)(d, c, s, l);
      var h = (o ? d : c).options.group.name;
      return a === !0 || typeof a == "string" && a === h || a.join && a.indexOf(h) > -1;
    };
  }
  var n = {}, i = t.group;
  (!i || vt(i) != "object") && (i = {
    name: i
  }), n.name = i.name, n.checkPull = r(i.pull, !0), n.checkPut = r(i.put), n.revertClone = i.revertClone, t.group = n;
}, zi = function() {
  !Xi && A && I(A, "display", "none");
}, Ji = function() {
  !Xi && A && I(A, "display", "");
};
qt && !qi && document.addEventListener("click", function(e) {
  if (Ct)
    return e.preventDefault(), e.stopPropagation && e.stopPropagation(), e.stopImmediatePropagation && e.stopImmediatePropagation(), Ct = !1, !1;
}, !0);
var He = function(t) {
  if (g) {
    t = t.touches ? t.touches[0] : t;
    var r = vo(t.clientX, t.clientY);
    if (r) {
      var n = {};
      for (var i in t)
        t.hasOwnProperty(i) && (n[i] = t[i]);
      n.target = n.rootEl = r, n.preventDefault = void 0, n.stopPropagation = void 0, r[se]._onDragOver(n);
    }
  }
}, Eo = function(t) {
  g && g.parentNode[se]._isOutsideThisEl(t.target);
};
function S(e, t) {
  if (!(e && e.nodeType && e.nodeType === 1))
    throw "Sortable: `el` must be an HTMLElement, not ".concat({}.toString.call(e));
  this.el = e, this.options = t = Te({}, t), e[se] = this;
  var r = {
    group: null,
    sort: !0,
    disabled: !1,
    store: null,
    handle: null,
    draggable: /^[uo]l$/i.test(e.nodeName) ? ">li" : ">*",
    swapThreshold: 1,
    // percentage; 0 <= x <= 1
    invertSwap: !1,
    // invert always
    invertedSwapThreshold: null,
    // will be set to same as swapThreshold if default
    removeCloneOnHide: !0,
    direction: function() {
      return Wi(e, this.options);
    },
    ghostClass: "sortable-ghost",
    chosenClass: "sortable-chosen",
    dragClass: "sortable-drag",
    ignore: "a, img",
    filter: null,
    preventOnFilter: !0,
    animation: 0,
    easing: null,
    setData: function(o, d) {
      o.setData("Text", d.textContent);
    },
    dropBubble: !1,
    dragoverBubble: !1,
    dataIdAttr: "data-id",
    delay: 0,
    delayOnTouchOnly: !1,
    touchStartThreshold: (Number.parseInt ? Number : window).parseInt(window.devicePixelRatio, 10) || 1,
    forceFallback: !1,
    fallbackClass: "sortable-fallback",
    fallbackOnBody: !1,
    fallbackTolerance: 0,
    fallbackOffset: {
      x: 0,
      y: 0
    },
    // Disabled on Safari: #1571; Enabled on Safari IOS: #2244
    supportPointer: S.supportPointer !== !1 && "PointerEvent" in window && (!rt || cn),
    emptyInsertThreshold: 5
  };
  ct.initializePlugins(this, e, r);
  for (var n in r)
    !(n in t) && (t[n] = r[n]);
  Yi(t);
  for (var i in this)
    i.charAt(0) === "_" && typeof this[i] == "function" && (this[i] = this[i].bind(this));
  this.nativeDraggable = t.forceFallback ? !1 : mo, this.nativeDraggable && (this.options.touchStartThreshold = 1), t.supportPointer ? M(e, "pointerdown", this._onTapStart) : (M(e, "mousedown", this._onTapStart), M(e, "touchstart", this._onTapStart)), this.nativeDraggable && (M(e, "dragover", this), M(e, "dragenter", this)), xt.push(this.el), t.store && t.store.get && this.sort(t.store.get(this) || []), Te(this, co());
}
S.prototype = /** @lends Sortable.prototype */
{
  constructor: S,
  _isOutsideThisEl: function(t) {
    !this.el.contains(t) && t !== this.el && (ke = null);
  },
  _getDirection: function(t, r) {
    return typeof this.options.direction == "function" ? this.options.direction.call(this, t, r, g) : this.options.direction;
  },
  _onTapStart: function(t) {
    if (t.cancelable) {
      var r = this, n = this.el, i = this.options, a = i.preventOnFilter, o = t.type, d = t.touches && t.touches[0] || t.pointerType && t.pointerType === "touch" && t, c = (d || t).target, s = t.target.shadowRoot && (t.path && t.path[0] || t.composedPath && t.composedPath()[0]) || c, l = i.filter;
      if (To(n), !g && !(/mousedown|pointerdown/.test(o) && t.button !== 0 || i.disabled) && !s.isContentEditable && !(!this.nativeDraggable && rt && c && c.tagName.toUpperCase() === "SELECT") && (c = pe(c, i.draggable, n, !1), !(c && c.animated) && Et !== c)) {
        if (Ze = he(c), it = he(c, i.draggable), typeof l == "function") {
          if (l.call(this, t, c, this)) {
            re({
              sortable: r,
              rootEl: s,
              name: "filter",
              targetEl: c,
              toEl: n,
              fromEl: n
            }), ae("filter", r, {
              evt: t
            }), a && t.preventDefault();
            return;
          }
        } else if (l && (l = l.split(",").some(function(u) {
          if (u = pe(s, u.trim(), n, !1), u)
            return re({
              sortable: r,
              rootEl: u,
              name: "filter",
              targetEl: c,
              fromEl: n,
              toEl: n
            }), ae("filter", r, {
              evt: t
            }), !0;
        }), l)) {
          a && t.preventDefault();
          return;
        }
        i.handle && !pe(s, i.handle, n, !1) || this._prepareDragStart(t, d, c);
      }
    }
  },
  _prepareDragStart: function(t, r, n) {
    var i = this, a = i.el, o = i.options, d = a.ownerDocument, c;
    if (n && !g && n.parentNode === a) {
      var s = z(n);
      if (Z = a, g = n, G = g.parentNode, Ve = g.nextSibling, Et = n, ht = o.group, S.dragged = g, Me = {
        target: g,
        clientX: (r || t).clientX,
        clientY: (r || t).clientY
      }, Dn = Me.clientX - s.left, On = Me.clientY - s.top, this._lastX = (r || t).clientX, this._lastY = (r || t).clientY, g.style["will-change"] = "all", c = function() {
        if (ae("delayEnded", i, {
          evt: t
        }), S.eventCanceled) {
          i._onDrop();
          return;
        }
        i._disableDelayedDragEvents(), !Tn && i.nativeDraggable && (g.draggable = !0), i._triggerDragStart(t, r), re({
          sortable: i,
          name: "choose",
          originalEvent: t
        }), ce(g, o.chosenClass, !0);
      }, o.ignore.split(",").forEach(function(l) {
        ji(g, l.trim(), er);
      }), M(d, "dragover", He), M(d, "mousemove", He), M(d, "touchmove", He), o.supportPointer ? (M(d, "pointerup", i._onDrop), !this.nativeDraggable && M(d, "pointercancel", i._onDrop)) : (M(d, "mouseup", i._onDrop), M(d, "touchend", i._onDrop), M(d, "touchcancel", i._onDrop)), Tn && this.nativeDraggable && (this.options.touchStartThreshold = 4, g.draggable = !0), ae("delayStart", this, {
        evt: t
      }), o.delay && (!o.delayOnTouchOnly || r) && (!this.nativeDraggable || !(lt || Ce))) {
        if (S.eventCanceled) {
          this._onDrop();
          return;
        }
        o.supportPointer ? (M(d, "pointerup", i._disableDelayedDrag), M(d, "pointercancel", i._disableDelayedDrag)) : (M(d, "mouseup", i._disableDelayedDrag), M(d, "touchend", i._disableDelayedDrag), M(d, "touchcancel", i._disableDelayedDrag)), M(d, "mousemove", i._delayedDragTouchMoveHandler), M(d, "touchmove", i._delayedDragTouchMoveHandler), o.supportPointer && M(d, "pointermove", i._delayedDragTouchMoveHandler), i._dragStartTimer = setTimeout(c, o.delay);
      } else
        c();
    }
  },
  _delayedDragTouchMoveHandler: function(t) {
    var r = t.touches ? t.touches[0] : t;
    Math.max(Math.abs(r.clientX - this._lastX), Math.abs(r.clientY - this._lastY)) >= Math.floor(this.options.touchStartThreshold / (this.nativeDraggable && window.devicePixelRatio || 1)) && this._disableDelayedDrag();
  },
  _disableDelayedDrag: function() {
    g && er(g), clearTimeout(this._dragStartTimer), this._disableDelayedDragEvents();
  },
  _disableDelayedDragEvents: function() {
    var t = this.el.ownerDocument;
    P(t, "mouseup", this._disableDelayedDrag), P(t, "touchend", this._disableDelayedDrag), P(t, "touchcancel", this._disableDelayedDrag), P(t, "pointerup", this._disableDelayedDrag), P(t, "pointercancel", this._disableDelayedDrag), P(t, "mousemove", this._delayedDragTouchMoveHandler), P(t, "touchmove", this._delayedDragTouchMoveHandler), P(t, "pointermove", this._delayedDragTouchMoveHandler);
  },
  _triggerDragStart: function(t, r) {
    r = r || t.pointerType == "touch" && t, !this.nativeDraggable || r ? this.options.supportPointer ? M(document, "pointermove", this._onTouchMove) : r ? M(document, "touchmove", this._onTouchMove) : M(document, "mousemove", this._onTouchMove) : (M(g, "dragend", this), M(Z, "dragstart", this._onDragStart));
    try {
      document.selection ? wt(function() {
        document.selection.empty();
      }) : window.getSelection().removeAllRanges();
    } catch {
    }
  },
  _dragStarted: function(t, r) {
    if (je = !1, Z && g) {
      ae("dragStarted", this, {
        evt: r
      }), this.nativeDraggable && M(document, "dragover", Eo);
      var n = this.options;
      !t && ce(g, n.dragClass, !1), ce(g, n.ghostClass, !0), S.active = this, t && this._appendGhost(), re({
        sortable: this,
        name: "start",
        originalEvent: r
      });
    } else
      this._nulling();
  },
  _emulateDragOver: function() {
    if (fe) {
      this._lastX = fe.clientX, this._lastY = fe.clientY, zi();
      for (var t = document.elementFromPoint(fe.clientX, fe.clientY), r = t; t && t.shadowRoot && (t = t.shadowRoot.elementFromPoint(fe.clientX, fe.clientY), t !== r); )
        r = t;
      if (g.parentNode[se]._isOutsideThisEl(t), r)
        do {
          if (r[se]) {
            var n = void 0;
            if (n = r[se]._onDragOver({
              clientX: fe.clientX,
              clientY: fe.clientY,
              target: t,
              rootEl: r
            }), n && !this.options.dragoverBubble)
              break;
          }
          t = r;
        } while (r = ki(r));
      Ji();
    }
  },
  _onTouchMove: function(t) {
    if (Me) {
      var r = this.options, n = r.fallbackTolerance, i = r.fallbackOffset, a = t.touches ? t.touches[0] : t, o = A && Be(A, !0), d = A && o && o.a, c = A && o && o.d, s = pt && ee && xn(ee), l = (a.clientX - Me.clientX + i.x) / (d || 1) + (s ? s[0] - Qt[0] : 0) / (d || 1), u = (a.clientY - Me.clientY + i.y) / (c || 1) + (s ? s[1] - Qt[1] : 0) / (c || 1);
      if (!S.active && !je) {
        if (n && Math.max(Math.abs(a.clientX - this._lastX), Math.abs(a.clientY - this._lastY)) < n)
          return;
        this._onDragStart(t, !0);
      }
      if (A) {
        o ? (o.e += l - (Jt || 0), o.f += u - (Kt || 0)) : o = {
          a: 1,
          b: 0,
          c: 0,
          d: 1,
          e: l,
          f: u
        };
        var h = "matrix(".concat(o.a, ",").concat(o.b, ",").concat(o.c, ",").concat(o.d, ",").concat(o.e, ",").concat(o.f, ")");
        I(A, "webkitTransform", h), I(A, "mozTransform", h), I(A, "msTransform", h), I(A, "transform", h), Jt = l, Kt = u, fe = a;
      }
      t.cancelable && t.preventDefault();
    }
  },
  _appendGhost: function() {
    if (!A) {
      var t = this.options.fallbackOnBody ? document.body : Z, r = z(g, !0, pt, !0, t), n = this.options;
      if (pt) {
        for (ee = t; I(ee, "position") === "static" && I(ee, "transform") === "none" && ee !== document; )
          ee = ee.parentNode;
        ee !== document.body && ee !== document.documentElement ? (ee === document && (ee = we()), r.top += ee.scrollTop, r.left += ee.scrollLeft) : ee = we(), Qt = xn(ee);
      }
      A = g.cloneNode(!0), ce(A, n.ghostClass, !1), ce(A, n.fallbackClass, !0), ce(A, n.dragClass, !0), I(A, "transition", ""), I(A, "transform", ""), I(A, "box-sizing", "border-box"), I(A, "margin", 0), I(A, "top", r.top), I(A, "left", r.left), I(A, "width", r.width), I(A, "height", r.height), I(A, "opacity", "0.8"), I(A, "position", pt ? "absolute" : "fixed"), I(A, "zIndex", "100000"), I(A, "pointerEvents", "none"), S.ghost = A, t.appendChild(A), I(A, "transform-origin", Dn / parseInt(A.style.width) * 100 + "% " + On / parseInt(A.style.height) * 100 + "%");
    }
  },
  _onDragStart: function(t, r) {
    var n = this, i = t.dataTransfer, a = n.options;
    if (ae("dragStart", this, {
      evt: t
    }), S.eventCanceled) {
      this._onDrop();
      return;
    }
    ae("setupClone", this), S.eventCanceled || (U = Bi(g), U.removeAttribute("id"), U.draggable = !1, U.style["will-change"] = "", this._hideClone(), ce(U, this.options.chosenClass, !1), S.clone = U), n.cloneId = wt(function() {
      ae("clone", n), !S.eventCanceled && (n.options.removeCloneOnHide || Z.insertBefore(U, g), n._hideClone(), re({
        sortable: n,
        name: "clone"
      }));
    }), !r && ce(g, a.dragClass, !0), r ? (Ct = !0, n._loopId = setInterval(n._emulateDragOver, 50)) : (P(document, "mouseup", n._onDrop), P(document, "touchend", n._onDrop), P(document, "touchcancel", n._onDrop), i && (i.effectAllowed = "move", a.setData && a.setData.call(n, i, g)), M(document, "drop", n), I(g, "transform", "translateZ(0)")), je = !0, n._dragStartId = wt(n._dragStarted.bind(n, r, t)), M(document, "selectstart", n), Je = !0, window.getSelection().removeAllRanges(), rt && I(document.body, "user-select", "none");
  },
  // Returns true - if no further action is needed (either inserted or another condition)
  _onDragOver: function(t) {
    var r = this.el, n = t.target, i, a, o, d = this.options, c = d.group, s = S.active, l = ht === c, u = d.sort, h = Q || s, p, E = this, f = !1;
    if (Qr) return;
    function $(R, y) {
      ae(R, E, ye({
        evt: t,
        isOwner: l,
        axis: p ? "vertical" : "horizontal",
        revert: o,
        dragRect: i,
        targetRect: a,
        canSort: u,
        fromSortable: h,
        target: n,
        completed: H,
        onMove: function(V, O) {
          return mt(Z, r, g, i, V, z(V), t, O);
        },
        changed: D
      }, y));
    }
    function _() {
      $("dragOverAnimationCapture"), E.captureAnimationState(), E !== h && h.captureAnimationState();
    }
    function H(R) {
      return $("dragOverCompleted", {
        insertion: R
      }), R && (l ? s._hideClone() : s._showClone(E), E !== h && (ce(g, Q ? Q.options.ghostClass : s.options.ghostClass, !1), ce(g, d.ghostClass, !0)), Q !== E && E !== S.active ? Q = E : E === S.active && Q && (Q = null), h === E && (E._ignoreWhileAnimating = n), E.animateAll(function() {
        $("dragOverAnimationComplete"), E._ignoreWhileAnimating = null;
      }), E !== h && (h.animateAll(), h._ignoreWhileAnimating = null)), (n === g && !g.animated || n === r && !n.animated) && (ke = null), !d.dragoverBubble && !t.rootEl && n !== document && (g.parentNode[se]._isOutsideThisEl(t.target), !R && He(t)), !d.dragoverBubble && t.stopPropagation && t.stopPropagation(), f = !0;
    }
    function D() {
      de = he(g), xe = he(g, d.draggable), re({
        sortable: E,
        name: "change",
        toEl: r,
        newIndex: de,
        newDraggableIndex: xe,
        originalEvent: t
      });
    }
    if (t.preventDefault !== void 0 && t.cancelable && t.preventDefault(), n = pe(n, d.draggable, r, !0), $("dragOver"), S.eventCanceled) return f;
    if (g.contains(t.target) || n.animated && n.animatingX && n.animatingY || E._ignoreWhileAnimating === n)
      return H(!1);
    if (Ct = !1, s && !d.disabled && (l ? u || (o = G !== Z) : Q === this || (this.lastPutMode = ht.checkPull(this, s, g, t)) && c.checkPut(this, s, g, t))) {
      if (p = this._getDirection(t, n) === "vertical", i = z(g), $("dragOverValid"), S.eventCanceled) return f;
      if (o)
        return G = Z, _(), this._hideClone(), $("revert"), S.eventCanceled || (Ve ? Z.insertBefore(g, Ve) : Z.appendChild(g)), H(!0);
      var q = dn(r, d.draggable);
      if (!q || Ro(t, p, this) && !q.animated) {
        if (q === g)
          return H(!1);
        if (q && r === t.target && (n = q), n && (a = z(n)), mt(Z, r, g, i, n, a, t, !!n) !== !1)
          return _(), q && q.nextSibling ? r.insertBefore(g, q.nextSibling) : r.appendChild(g), G = r, D(), H(!0);
      } else if (q && yo(t, p, this)) {
        var B = Ge(r, 0, d, !0);
        if (B === g)
          return H(!1);
        if (n = B, a = z(n), mt(Z, r, g, i, n, a, t, !1) !== !1)
          return _(), r.insertBefore(g, B), G = r, D(), H(!0);
      } else if (n.parentNode === r) {
        a = z(n);
        var L = 0, k, T = g.parentNode !== r, J = !go(g.animated && g.toRect || i, n.animated && n.toRect || a, p), $e = p ? "top" : "left", ue = Cn(n, "top", "top") || Cn(g, "top", "top"), Ie = ue ? ue.scrollTop : void 0;
        ke !== n && (k = a[$e], ot = !1, ft = !J && d.invertSwap || T), L = $o(t, n, a, p, J ? 1 : d.swapThreshold, d.invertedSwapThreshold == null ? d.swapThreshold : d.invertedSwapThreshold, ft, ke === n);
        var le;
        if (L !== 0) {
          var v = he(g);
          do
            v -= L, le = G.children[v];
          while (le && (I(le, "display") === "none" || le === A));
        }
        if (L === 0 || le === n)
          return H(!1);
        ke = n, at = L;
        var m = n.nextElementSibling, w = !1;
        w = L === 1;
        var b = mt(Z, r, g, i, n, a, t, w);
        if (b !== !1)
          return (b === 1 || b === -1) && (w = b === 1), Qr = !0, setTimeout(wo, 30), _(), w && !m ? r.appendChild(g) : n.parentNode.insertBefore(g, w ? m : n), ue && Ui(ue, 0, Ie - ue.scrollTop), G = g.parentNode, k !== void 0 && !ft && (bt = Math.abs(k - z(n)[$e])), D(), H(!0);
      }
      if (r.contains(g))
        return H(!1);
    }
    return !1;
  },
  _ignoreWhileAnimating: null,
  _offMoveEvents: function() {
    P(document, "mousemove", this._onTouchMove), P(document, "touchmove", this._onTouchMove), P(document, "pointermove", this._onTouchMove), P(document, "dragover", He), P(document, "mousemove", He), P(document, "touchmove", He);
  },
  _offUpEvents: function() {
    var t = this.el.ownerDocument;
    P(t, "mouseup", this._onDrop), P(t, "touchend", this._onDrop), P(t, "pointerup", this._onDrop), P(t, "pointercancel", this._onDrop), P(t, "touchcancel", this._onDrop), P(document, "selectstart", this);
  },
  _onDrop: function(t) {
    var r = this.el, n = this.options;
    if (de = he(g), xe = he(g, n.draggable), ae("drop", this, {
      evt: t
    }), G = g && g.parentNode, de = he(g), xe = he(g, n.draggable), S.eventCanceled) {
      this._nulling();
      return;
    }
    je = !1, ft = !1, ot = !1, clearInterval(this._loopId), clearTimeout(this._dragStartTimer), en(this.cloneId), en(this._dragStartId), this.nativeDraggable && (P(document, "drop", this), P(r, "dragstart", this._onDragStart)), this._offMoveEvents(), this._offUpEvents(), rt && I(document.body, "user-select", ""), I(g, "transform", ""), t && (Je && (t.cancelable && t.preventDefault(), !n.dropBubble && t.stopPropagation()), A && A.parentNode && A.parentNode.removeChild(A), (Z === G || Q && Q.lastPutMode !== "clone") && U && U.parentNode && U.parentNode.removeChild(U), g && (this.nativeDraggable && P(g, "dragend", this), er(g), g.style["will-change"] = "", Je && !je && ce(g, Q ? Q.options.ghostClass : this.options.ghostClass, !1), ce(g, this.options.chosenClass, !1), re({
      sortable: this,
      name: "unchoose",
      toEl: G,
      newIndex: null,
      newDraggableIndex: null,
      originalEvent: t
    }), Z !== G ? (de >= 0 && (re({
      rootEl: G,
      name: "add",
      toEl: G,
      fromEl: Z,
      originalEvent: t
    }), re({
      sortable: this,
      name: "remove",
      toEl: G,
      originalEvent: t
    }), re({
      rootEl: G,
      name: "sort",
      toEl: G,
      fromEl: Z,
      originalEvent: t
    }), re({
      sortable: this,
      name: "sort",
      toEl: G,
      originalEvent: t
    })), Q && Q.save()) : de !== Ze && de >= 0 && (re({
      sortable: this,
      name: "update",
      toEl: G,
      originalEvent: t
    }), re({
      sortable: this,
      name: "sort",
      toEl: G,
      originalEvent: t
    })), S.active && ((de == null || de === -1) && (de = Ze, xe = it), re({
      sortable: this,
      name: "end",
      toEl: G,
      originalEvent: t
    }), this.save()))), this._nulling();
  },
  _nulling: function() {
    ae("nulling", this), Z = g = G = A = Ve = U = Et = De = Me = fe = Je = de = xe = Ze = it = ke = at = Q = ht = S.dragged = S.ghost = S.clone = S.active = null, Dt.forEach(function(t) {
      t.checked = !0;
    }), Dt.length = Jt = Kt = 0;
  },
  handleEvent: function(t) {
    switch (t.type) {
      case "drop":
      case "dragend":
        this._onDrop(t);
        break;
      case "dragenter":
      case "dragover":
        g && (this._onDragOver(t), bo(t));
        break;
      case "selectstart":
        t.preventDefault();
        break;
    }
  },
  /**
   * Serializes the item into an array of string.
   * @returns {String[]}
   */
  toArray: function() {
    for (var t = [], r, n = this.el.children, i = 0, a = n.length, o = this.options; i < a; i++)
      r = n[i], pe(r, o.draggable, this.el, !1) && t.push(r.getAttribute(o.dataIdAttr) || So(r));
    return t;
  },
  /**
   * Sorts the elements according to the array.
   * @param  {String[]}  order  order of the items
   */
  sort: function(t, r) {
    var n = {}, i = this.el;
    this.toArray().forEach(function(a, o) {
      var d = i.children[o];
      pe(d, this.options.draggable, i, !1) && (n[a] = d);
    }, this), r && this.captureAnimationState(), t.forEach(function(a) {
      n[a] && (i.removeChild(n[a]), i.appendChild(n[a]));
    }), r && this.animateAll();
  },
  /**
   * Save the current sorting
   */
  save: function() {
    var t = this.options.store;
    t && t.set && t.set(this);
  },
  /**
   * For each element in the set, get the first element that matches the selector by testing the element itself and traversing up through its ancestors in the DOM tree.
   * @param   {HTMLElement}  el
   * @param   {String}       [selector]  default: `options.draggable`
   * @returns {HTMLElement|null}
   */
  closest: function(t, r) {
    return pe(t, r || this.options.draggable, this.el, !1);
  },
  /**
   * Set/get option
   * @param   {string} name
   * @param   {*}      [value]
   * @returns {*}
   */
  option: function(t, r) {
    var n = this.options;
    if (r === void 0)
      return n[t];
    var i = ct.modifyOption(this, t, r);
    typeof i < "u" ? n[t] = i : n[t] = r, t === "group" && Yi(n);
  },
  /**
   * Destroy
   */
  destroy: function() {
    ae("destroy", this);
    var t = this.el;
    t[se] = null, P(t, "mousedown", this._onTapStart), P(t, "touchstart", this._onTapStart), P(t, "pointerdown", this._onTapStart), this.nativeDraggable && (P(t, "dragover", this), P(t, "dragenter", this)), Array.prototype.forEach.call(t.querySelectorAll("[draggable]"), function(r) {
      r.removeAttribute("draggable");
    }), this._onDrop(), this._disableDelayedDragEvents(), xt.splice(xt.indexOf(this.el), 1), this.el = t = null;
  },
  _hideClone: function() {
    if (!De) {
      if (ae("hideClone", this), S.eventCanceled) return;
      I(U, "display", "none"), this.options.removeCloneOnHide && U.parentNode && U.parentNode.removeChild(U), De = !0;
    }
  },
  _showClone: function(t) {
    if (t.lastPutMode !== "clone") {
      this._hideClone();
      return;
    }
    if (De) {
      if (ae("showClone", this), S.eventCanceled) return;
      g.parentNode == Z && !this.options.group.revertClone ? Z.insertBefore(U, g) : Ve ? Z.insertBefore(U, Ve) : Z.appendChild(U), this.options.group.revertClone && this.animate(g, U), I(U, "display", ""), De = !1;
    }
  }
};
function bo(e) {
  e.dataTransfer && (e.dataTransfer.dropEffect = "move"), e.cancelable && e.preventDefault();
}
function mt(e, t, r, n, i, a, o, d) {
  var c, s = e[se], l = s.options.onMove, u;
  return window.CustomEvent && !Ce && !lt ? c = new CustomEvent("move", {
    bubbles: !0,
    cancelable: !0
  }) : (c = document.createEvent("Event"), c.initEvent("move", !0, !0)), c.to = t, c.from = e, c.dragged = r, c.draggedRect = n, c.related = i || t, c.relatedRect = a || z(t), c.willInsertAfter = d, c.originalEvent = o, e.dispatchEvent(c), l && (u = l.call(s, c, o)), u;
}
function er(e) {
  e.draggable = !1;
}
function wo() {
  Qr = !1;
}
function yo(e, t, r) {
  var n = z(Ge(r.el, 0, r.options, !0)), i = Gi(r.el, r.options, A), a = 10;
  return t ? e.clientX < i.left - a || e.clientY < n.top && e.clientX < n.right : e.clientY < i.top - a || e.clientY < n.bottom && e.clientX < n.left;
}
function Ro(e, t, r) {
  var n = z(dn(r.el, r.options.draggable)), i = Gi(r.el, r.options, A), a = 10;
  return t ? e.clientX > i.right + a || e.clientY > n.bottom && e.clientX > n.left : e.clientY > i.bottom + a || e.clientX > n.right && e.clientY > n.top;
}
function $o(e, t, r, n, i, a, o, d) {
  var c = n ? e.clientY : e.clientX, s = n ? r.height : r.width, l = n ? r.top : r.left, u = n ? r.bottom : r.right, h = !1;
  if (!o) {
    if (d && bt < s * i) {
      if (!ot && (at === 1 ? c > l + s * a / 2 : c < u - s * a / 2) && (ot = !0), ot)
        h = !0;
      else if (at === 1 ? c < l + bt : c > u - bt)
        return -at;
    } else if (c > l + s * (1 - i) / 2 && c < u - s * (1 - i) / 2)
      return Io(t);
  }
  return h = h || o, h && (c < l + s * a / 2 || c > u - s * a / 2) ? c > l + s / 2 ? 1 : -1 : 0;
}
function Io(e) {
  return he(g) < he(e) ? 1 : -1;
}
function So(e) {
  for (var t = e.tagName + e.className + e.src + e.href + e.textContent, r = t.length, n = 0; r--; )
    n += t.charCodeAt(r);
  return n.toString(36);
}
function To(e) {
  Dt.length = 0;
  for (var t = e.getElementsByTagName("input"), r = t.length; r--; ) {
    var n = t[r];
    n.checked && Dt.push(n);
  }
}
function wt(e) {
  return setTimeout(e, 0);
}
function en(e) {
  return clearTimeout(e);
}
qt && M(document, "touchmove", function(e) {
  (S.active || je) && e.cancelable && e.preventDefault();
});
S.utils = {
  on: M,
  off: P,
  css: I,
  find: ji,
  is: function(t, r) {
    return !!pe(t, r, t, !1);
  },
  extend: so,
  throttle: Zi,
  closest: pe,
  toggleClass: ce,
  clone: Bi,
  index: he,
  nextTick: wt,
  cancelNextTick: en,
  detectDirection: Wi,
  getChild: Ge,
  expando: se
};
S.get = function(e) {
  return e[se];
};
S.mount = function() {
  for (var e = arguments.length, t = new Array(e), r = 0; r < e; r++)
    t[r] = arguments[r];
  t[0].constructor === Array && (t = t[0]), t.forEach(function(n) {
    if (!n.prototype || !n.prototype.constructor)
      throw "Sortable: Mounted plugin must be a constructor function, not ".concat({}.toString.call(n));
    n.utils && (S.utils = ye(ye({}, S.utils), n.utils)), ct.mount(n);
  });
};
S.create = function(e, t) {
  return new S(e, t);
};
S.version = ao;
var Y = [], Ke, tn, rn = !1, tr, rr, Ot, Qe;
function Ao() {
  function e() {
    this.defaults = {
      scroll: !0,
      forceAutoScrollFallback: !1,
      scrollSensitivity: 30,
      scrollSpeed: 10,
      bubbleScroll: !0
    };
    for (var t in this)
      t.charAt(0) === "_" && typeof this[t] == "function" && (this[t] = this[t].bind(this));
  }
  return e.prototype = {
    dragStarted: function(r) {
      var n = r.originalEvent;
      this.sortable.nativeDraggable ? M(document, "dragover", this._handleAutoScroll) : this.options.supportPointer ? M(document, "pointermove", this._handleFallbackAutoScroll) : n.touches ? M(document, "touchmove", this._handleFallbackAutoScroll) : M(document, "mousemove", this._handleFallbackAutoScroll);
    },
    dragOverCompleted: function(r) {
      var n = r.originalEvent;
      !this.options.dragOverBubble && !n.rootEl && this._handleAutoScroll(n);
    },
    drop: function() {
      this.sortable.nativeDraggable ? P(document, "dragover", this._handleAutoScroll) : (P(document, "pointermove", this._handleFallbackAutoScroll), P(document, "touchmove", this._handleFallbackAutoScroll), P(document, "mousemove", this._handleFallbackAutoScroll)), Pn(), yt(), lo();
    },
    nulling: function() {
      Ot = tn = Ke = rn = Qe = tr = rr = null, Y.length = 0;
    },
    _handleFallbackAutoScroll: function(r) {
      this._handleAutoScroll(r, !0);
    },
    _handleAutoScroll: function(r, n) {
      var i = this, a = (r.touches ? r.touches[0] : r).clientX, o = (r.touches ? r.touches[0] : r).clientY, d = document.elementFromPoint(a, o);
      if (Ot = r, n || this.options.forceAutoScrollFallback || lt || Ce || rt) {
        nr(r, this.options, d, n);
        var c = Oe(d, !0);
        rn && (!Qe || a !== tr || o !== rr) && (Qe && Pn(), Qe = setInterval(function() {
          var s = Oe(document.elementFromPoint(a, o), !0);
          s !== c && (c = s, yt()), nr(r, i.options, s, n);
        }, 10), tr = a, rr = o);
      } else {
        if (!this.options.bubbleScroll || Oe(d, !0) === we()) {
          yt();
          return;
        }
        nr(r, this.options, Oe(d, !1), !1);
      }
    }
  }, Te(e, {
    pluginName: "scroll",
    initializeByDefault: !0
  });
}
function yt() {
  Y.forEach(function(e) {
    clearInterval(e.pid);
  }), Y = [];
}
function Pn() {
  clearInterval(Qe);
}
var nr = Zi(function(e, t, r, n) {
  if (t.scroll) {
    var i = (e.touches ? e.touches[0] : e).clientX, a = (e.touches ? e.touches[0] : e).clientY, o = t.scrollSensitivity, d = t.scrollSpeed, c = we(), s = !1, l;
    tn !== r && (tn = r, yt(), Ke = t.scroll, l = t.scrollFn, Ke === !0 && (Ke = Oe(r, !0)));
    var u = 0, h = Ke;
    do {
      var p = h, E = z(p), f = E.top, $ = E.bottom, _ = E.left, H = E.right, D = E.width, q = E.height, B = void 0, L = void 0, k = p.scrollWidth, T = p.scrollHeight, J = I(p), $e = p.scrollLeft, ue = p.scrollTop;
      p === c ? (B = D < k && (J.overflowX === "auto" || J.overflowX === "scroll" || J.overflowX === "visible"), L = q < T && (J.overflowY === "auto" || J.overflowY === "scroll" || J.overflowY === "visible")) : (B = D < k && (J.overflowX === "auto" || J.overflowX === "scroll"), L = q < T && (J.overflowY === "auto" || J.overflowY === "scroll"));
      var Ie = B && (Math.abs(H - i) <= o && $e + D < k) - (Math.abs(_ - i) <= o && !!$e), le = L && (Math.abs($ - a) <= o && ue + q < T) - (Math.abs(f - a) <= o && !!ue);
      if (!Y[u])
        for (var v = 0; v <= u; v++)
          Y[v] || (Y[v] = {});
      (Y[u].vx != Ie || Y[u].vy != le || Y[u].el !== p) && (Y[u].el = p, Y[u].vx = Ie, Y[u].vy = le, clearInterval(Y[u].pid), (Ie != 0 || le != 0) && (s = !0, Y[u].pid = setInterval((function() {
        n && this.layer === 0 && S.active._onTouchMove(Ot);
        var m = Y[this.layer].vy ? Y[this.layer].vy * d : 0, w = Y[this.layer].vx ? Y[this.layer].vx * d : 0;
        typeof l == "function" && l.call(S.dragged.parentNode[se], w, m, e, Ot, Y[this.layer].el) !== "continue" || Ui(Y[this.layer].el, w, m);
      }).bind({
        layer: u
      }), 24))), u++;
    } while (t.bubbleScroll && h !== c && (h = Oe(h, !1)));
    rn = s;
  }
}, 30), Ki = function(t) {
  var r = t.originalEvent, n = t.putSortable, i = t.dragEl, a = t.activeSortable, o = t.dispatchSortableEvent, d = t.hideGhostForTarget, c = t.unhideGhostForTarget;
  if (r) {
    var s = n || a;
    d();
    var l = r.changedTouches && r.changedTouches.length ? r.changedTouches[0] : r, u = document.elementFromPoint(l.clientX, l.clientY);
    c(), s && !s.el.contains(u) && (o("spill"), this.onSpill({
      dragEl: i,
      putSortable: n
    }));
  }
};
function un() {
}
un.prototype = {
  startIndex: null,
  dragStart: function(t) {
    var r = t.oldDraggableIndex;
    this.startIndex = r;
  },
  onSpill: function(t) {
    var r = t.dragEl, n = t.putSortable;
    this.sortable.captureAnimationState(), n && n.captureAnimationState();
    var i = Ge(this.sortable.el, this.startIndex, this.options);
    i ? this.sortable.el.insertBefore(r, i) : this.sortable.el.appendChild(r), this.sortable.animateAll(), n && n.animateAll();
  },
  drop: Ki
};
Te(un, {
  pluginName: "revertOnSpill"
});
function hn() {
}
hn.prototype = {
  onSpill: function(t) {
    var r = t.dragEl, n = t.putSortable, i = n || this.sortable;
    i.captureAnimationState(), r.parentNode && r.parentNode.removeChild(r), i.animateAll();
  },
  drop: Ki
};
Te(hn, {
  pluginName: "removeOnSpill"
});
S.mount(new Ao());
S.mount(hn, un);
var Co = Object.defineProperty, xo = Object.getOwnPropertyDescriptor, Xe = (e, t, r, n) => {
  for (var i = n > 1 ? void 0 : n ? xo(t, r) : t, a = e.length - 1, o; a >= 0; a--)
    (o = e[a]) && (i = (n ? o(t, r, i) : o(i)) || i);
  return n && i && Co(t, r, i), i;
};
let Re = class extends _t {
  constructor() {
    super(...arguments), this.sortableInitialized = !1, this.modeRadioButtonItems = [], this.devToolsDataLoading = !1, this.devToolsDataLoaded = !1, this.isDragging = !1, this.dragPreparing = !1, this.dragStartX = 0, this.dragStartY = 0, this.toolbarStartRight = 0, this.toolbarStartTop = 0, this.wasDraggedInLastInteraction = !1, this.dragThreshold = 5, this.transitionStart = (e) => {
      e.propertyName === "width" && this.setAttribute(Re.TRANSITION_STATUS_ATTR_KEY, "started");
    }, this.transitionEnd = (e) => {
      e.propertyName === "width" && (this.setAttribute(Re.TRANSITION_STATUS_ATTR_KEY, "ended"), this.constrainToViewport());
    }, this.handleMouseDown = (e) => {
      if (e.button !== 0)
        return;
      const t = this.querySelector("#devtools-button");
      if (!t || !(e.target instanceof Node) || !t.contains(e.target))
        return;
      this.dragPreparing = !0, this.dragStartX = e.clientX, this.dragStartY = e.clientY;
      const r = this.getBoundingClientRect();
      this.toolbarStartRight = window.innerWidth - r.right, this.toolbarStartTop = r.top, document.addEventListener("mousemove", this.handleMouseMove), document.addEventListener("mouseup", this.handleMouseUp);
    }, this.handleMouseMove = (e) => {
      if (!this.dragPreparing && !this.isDragging)
        return;
      const t = e.clientX - this.dragStartX, r = e.clientY - this.dragStartY, n = Math.sqrt(t * t + r * r);
      if (this.dragPreparing && n > this.dragThreshold && (this.isDragging = !0, this.dragPreparing = !1, this.style.transition = "none"), this.isDragging) {
        let i = this.toolbarStartRight - t, a = this.toolbarStartTop + r;
        const o = this.getBoundingClientRect(), d = o.width, c = o.height;
        i = Math.max(0, i), window.innerWidth - i - d < 0 && (i = window.innerWidth - d), a = Math.max(0, a);
        const l = window.innerHeight - c;
        a = Math.min(a, l), this.style.right = `${i}px`, this.style.top = `${a}px`, this.style.bottom = "auto", this.style.left = "auto";
      }
    }, this.handleMouseUp = () => {
      const e = this.isDragging;
      if (this.isDragging = !1, this.dragPreparing = !1, this.wasDraggedInLastInteraction = e, e) {
        this.style.transition = "";
        const t = this.getBoundingClientRect(), r = window.innerWidth - t.right, n = t.top;
        Xt.saveToolbarPosition(r, n);
      }
      document.removeEventListener("mousemove", this.handleMouseMove), document.removeEventListener("mouseup", this.handleMouseUp), setTimeout(() => {
        this.wasDraggedInLastInteraction = !1;
      }, 10);
    }, this.handleClick = (e) => {
      this.wasDraggedInLastInteraction && (e.stopPropagation(), e.preventDefault());
    }, this.handleWindowResize = () => {
      this.constrainToViewport(!0);
    }, this.onSortEndEvent = (e, t, r) => {
      if (!(e.item instanceof HTMLElement))
        return;
      const n = e.item.dataset.panelTag;
      if (!n || !N.getPanelByTag(n))
        return;
      const a = this.getOrderByTag(t);
      N.reOrderPanels(a, r);
    };
  }
  // Minimum pixels to move before dragging starts
  connectedCallback() {
    super.connectedCallback(), this.popover = "manual", this.modeRadioButtonItems = Object.entries(wa).map(([t, r]) => ({
      value: t,
      label: r.label,
      order: r.toolbarOrder,
      icon: r.toolbarIcon
    })), this.modeRadioButtonItems.sort((t, r) => t.order - r.order), this.classList.add(
      "border",
      "bg-gray-1",
      "dark:bg-gray-5",
      "bottom-4",
      "duration-300",
      "flex",
      "justify-end",
      "overflow-hidden",
      "rounded-xl",
      "shadow-xl",
      "transition-all",
      "z-100"
    ), this.style.position = "fixed";
    const e = Xt.getToolbarPosition();
    if (e) {
      const t = e.right, r = e.top;
      this.style.right = `${t}px`, this.style.top = `${r}px`, this.style.bottom = "auto", this.style.left = "auto";
    }
    this.addEventListener("transitionstart", this.transitionStart), this.addEventListener("transitionend", this.transitionEnd), this.addEventListener("mousedown", this.handleMouseDown), this.addEventListener("click", this.handleClick, !0), window.addEventListener("resize", this.handleWindowResize), this.reaction(
      () => F.userInfo,
      () => {
        F.userInfo && this.devToolsPopover.requestContentUpdate();
      }
    ), this.reaction(
      () => F.activeMode,
      () => {
        this.queryModeRadioButtons().forEach((r) => {
          const n = r;
          n._setFocused && n._setFocused(!1);
        });
        const t = this.queryRadioButtonForMode(F.activeMode);
        t?._setFocused && t._setFocused(!0);
      }
    );
  }
  disconnectedCallback() {
    super.disconnectedCallback(), this.removeEventListener("transitionstart", this.transitionStart), this.removeEventListener("transitionend", this.transitionEnd), this.removeEventListener("mousedown", this.handleMouseDown), this.removeEventListener("click", this.handleClick, !0), document.removeEventListener("mousemove", this.handleMouseMove), document.removeEventListener("mouseup", this.handleMouseUp), window.removeEventListener("resize", this.handleWindowResize);
  }
  constrainToViewport(e = !1) {
    if (!this.style.right || this.style.right === "")
      return;
    const t = this.getBoundingClientRect(), r = t.width, n = t.height;
    if (r === 0 || n === 0)
      return;
    let i = parseFloat(this.style.right), a = parseFloat(this.style.top || "0");
    const o = i, d = a;
    i = Math.max(0, i), window.innerWidth - i - r < 0 && (i = window.innerWidth - r), a = Math.max(0, a);
    const s = window.innerHeight - n;
    a = Math.min(a, s), this.style.right = `${i}px`, this.style.top = `${a}px`, e && (i !== o || a !== d) && Xt.saveToolbarPosition(i, a);
  }
  createRenderRoot() {
    return this;
  }
  updated(e) {
    super.updated(e);
    const r = [...this.querySelectorAll(
      "#mode-panel-icon-container, #mode-icon-container, #common-mode-panel-icon-container"
    )].reduce((n, i) => n + i.offsetWidth, 0);
    this.style.width = `${r}px`, this.modePanelIconContainer !== null && !this.sortableInitialized && new S(this.modePanelIconContainer, {
      animation: 150,
      draggable: ".panel-icon-button",
      onEnd: (n) => this.onSortEndEvent(n, this.modePanelIconContainer, F.activeMode)
    });
  }
  render() {
    const e = N.panels;
    return x`
      <div class="flex pe-1" id="mode-panel-icon-container" ?hidden="${F.activeMode === "play"}">
        ${F.activeMode ? this.renderPanelList(e, F.activeMode) : me}
      </div>
      <div class="flex pe-1" id="mode-icon-container">${this.renderCopilotModeButtons()}</div>
      <div class="flex" id="common-mode-panel-icon-container">
        <vaadin-button aria-label="DevTools" class="max-w-6" id="devtools-button" theme="icon tertiary toolbar">
          <vaadin-icon .svg="${ne.moreVert}"></vaadin-icon>
          <vaadin-tooltip slot="tooltip" text="Open menu"></vaadin-tooltip>
        </vaadin-button>
        <vaadin-popover
          @opened-changed="${(t) => {
      t.detail.value && !this.devToolsDataLoaded && (this.devToolsDataLoading = !0, import("./copilot-devtools-BYPL2nBN.js").then(() => {
        ya(), N.restorePanelsFromStorage(), Ra(), $a(), this.devToolsDataLoading = !1, this.devToolsDataLoaded = !0;
      }));
    }}"
          position="top"
          for="devtools-button"
          id="devtools-popover"
          modal
          theme="arrow no-padding"
          width="360">
          <!--          TODO move loader here so it should not wait for devtools to be imported -->
          <copilot-devtools .loading="${this.devToolsDataLoading}"></copilot-devtools>
        </vaadin-popover>
      </div>
    `;
  }
  renderPanelList(e, t) {
    return x`
      ${et(
      e.filter((r) => r.experimental?.enabled() !== !1).filter((r) => r.toolbarOptions?.allowedModesWithOrder?.[t] !== void 0).sort((r, n) => r.toolbarOptions.allowedModesWithOrder[t] - n.toolbarOptions.allowedModesWithOrder[t]),
      (r) => r.tag,
      (r) => this.renderPanelIcon(r)
    )}
    `;
  }
  renderCopilotModeButtons() {
    return x`
      <vaadin-radio-group
        theme="toolbar"
        .value="${F.activeMode}"
        @change="${(e) => {
      const t = e.target.value;
      F.setActiveMode(t, !0);
    }}">
        ${et(
      this.modeRadioButtonItems,
      (e) => e.value,
      (e) => x`
            <vaadin-radio-button .value="${e.value}" .id="${e.value}-mode-radio-btn">
              <label slot="label">
                <vaadin-icon .svg="${ne[e.icon]}"></vaadin-icon>
              </label>
            </vaadin-radio-button>
          `
    )}
      </vaadin-radio-group>
      ${et(
      this.modeRadioButtonItems,
      (e) => e.value,
      (e) => x`
          <vaadin-tooltip for="${e.value}-mode-radio-btn" text="${e.label} Mode"></vaadin-tooltip>
        `
    )}
    `;
  }
  renderPanelIcon(e) {
    const t = this.getButtonId(e);
    return x`
      <vaadin-button
        aria-expanded="${N.isOpenedPanel(e.tag)}"
        aria-label="${e.header}"
        class="panel-icon-button relative"
        data-panel-tag="${e.tag}"
        id="${t}"
        theme="icon tertiary toolbar"
        @click=${(r) => {
      if (N.isOpenedPanel(e.tag)) {
        const o = this.getRootNode().querySelector("copilot-panel-manager")?.getDialogByPanelTag(e.tag)?.querySelector(e.tag);
        o?.requestClose ? o.requestClose(() => N.closePanel(e.tag)) : N.closePanel(e.tag);
      } else
        N.attentionRequiredPanelTag === e.tag && N.clearAttention(), N.openPanel(e.tag);
    }}>
        <vaadin-icon .svg="${ne[e.toolbarOptions.iconKey]}"></vaadin-icon>
        <vaadin-tooltip
          slot="tooltip"
          text="${e.header}${N.attentionRequiredPanelTag === e.tag ? " – Attention Required" : ""}"></vaadin-tooltip>
        ${N.attentionRequiredPanelTag === e.tag ? x`<span
                aria-hidden="true"
                class="absolute animate-ping bg-amber-11 end-0.5 rounded-full size-2 top-0.5"></span>
              <span
                aria-hidden="true"
                class="absolute bg-amber-11 border border-gray-1 dark:border-gray-5 box-border end-0.5 rounded-full size-2 top-0.5"></span>` : me}
      </vaadin-button>
    `;
  }
  getButtonId(e) {
    return `${e.tag}-toolbar-btn`;
  }
  getOrderByTag(e) {
    const t = /* @__PURE__ */ new Map();
    return [...e.querySelectorAll("[data-panel-tag]")].forEach((r, n) => {
      t.set(r.dataset.panelTag, n);
    }), t;
  }
  queryModeRadioButtons() {
    return Array.from(this.querySelectorAll("vaadin-radio-button"));
  }
  queryRadioButtonForMode(e) {
    return this.querySelector(`vaadin-radio-button#${e}-mode-radio-btn`);
  }
};
Re.TRANSITION_STATUS_ATTR_KEY = "transition-status";
Xe([
  Oi("#mode-panel-icon-container")
], Re.prototype, "modePanelIconContainer", 2);
Xe([
  Oi("#devtools-popover")
], Re.prototype, "devToolsPopover", 2);
Xe([
  ve()
], Re.prototype, "modeRadioButtonItems", 2);
Xe([
  ve()
], Re.prototype, "devToolsDataLoading", 2);
Xe([
  ve()
], Re.prototype, "devToolsDataLoaded", 2);
Re = Xe([
  Ne("copilot-toolbar")
], Re);
function Do() {
  const e = [];
  F.userInfo?.vaadiner && e.push({
    name: "Vaadin Employee",
    value: "true",
    booleanInfo: { ariaLabel: "Yes" }
  });
  const t = [
    ...K.serverVersions.map((r) => ({ name: r.name, value: r.version })),
    ...e,
    ...K.clientVersions.map((r) => ({ name: r.name, value: r.version }))
  ];
  return t.forEach((r) => {
    r.name === "Frontend Hotswap" && (r.value === "true" ? r.booleanInfo = { ariaLabel: "Enabled", text: "Vite" } : r.value === "false" && (r.booleanInfo = { ariaLabel: "Disabled", text: "Pre-Built Bundle" }));
  }), K.springSecurityEnabled && t.push({ name: "Spring Security", value: "true", booleanInfo: { ariaLabel: "Enabled" } }), K.springJpaDataEnabled && t.push({ name: "Spring Data JPA", value: "true", booleanInfo: { ariaLabel: "Enabled" } }), t.push(..._o()), Oo(t), t;
}
function Oo(e) {
  for (const t of e)
    t.value === "true" ? (t.value = !0, t.booleanInfo ? t.booleanInfo.ariaLabel || (t.booleanInfo.ariaLabel = "Enabled") : t.booleanInfo = { ariaLabel: "Enabled" }) : t.value === "false" && (t.value = !1, t.booleanInfo ? t.booleanInfo.ariaLabel || (t.booleanInfo.ariaLabel = "Disabled") : t.booleanInfo = { ariaLabel: "Disabled" });
}
function _o() {
  const e = [], t = on(), r = Qi(F.idePluginState), n = ea(t);
  return e.push({
    name: "Java Hotswap",
    value: t === "success",
    booleanInfo: {
      ariaLabel: t === "success" ? "Enabled" : "Disabled",
      text: n
    }
  }), Rt() !== "unsupported" && e.push({
    name: "IDE Plugin",
    value: Rt() === "success",
    booleanInfo: {
      ariaLabel: Rt() === "success" ? "Installed" : "Not Installed",
      text: r
    }
  }), e;
}
function Po(e) {
  const t = F.projectInfoEntries;
  if (!t)
    throw new Error("Unable to load project info entries");
  const i = t.map((a) => ({ key: a.name, value: a.value })).filter((a) => a.key !== "Live reload").filter((a) => !a.key.startsWith("Vaadin Emplo")).filter((a) => a.key !== "Development Workflow").map((a) => {
    const { key: o } = a;
    let { value: d } = a;
    if (o === "IDE Plugin")
      d = Qi(F.idePluginState) ?? "false";
    else if (o === "Java Hotswap") {
      const c = K.jdkInfo?.jrebel, s = on();
      c && s === "success" ? d = "JRebel is in use" : d = ea(s);
    }
    return o === "Frontend Hotswap" && (d === "true" || d === !0 ? d = "Vite" : (d === "false" || d === !1) && (d = "Pre-Built Bundle")), `${o}: ${d}`;
  }).join(`
`);
  return e && _e({
    type: oe.INFORMATION,
    message: "Environment information copied to clipboard"
  }), i.trim();
}
function Rt() {
  return F.idePluginState !== void 0 && !F.idePluginState.active ? "warning" : "success";
}
function Qi(e) {
  if (Rt() !== "success")
    return "Not installed";
  if (e?.version) {
    let t = null;
    return e?.ide && (e?.ide === "intellij" ? t = "IntelliJ" : e?.ide === "vscode" ? t = "VS Code" : e?.ide === "eclipse" && (t = "Eclipse")), t ? `${e?.version} ${t}` : e?.version;
  }
  return "Not installed";
}
function ea(e) {
  return e === "success" ? "Java Hotswap is enabled" : e === "error" ? "Hotswap is partially enabled" : "Hotswap is disabled";
}
Ia(
  () => [
    K.serverVersions,
    K.clientVersions,
    F.userInfo,
    K.springSecurityEnabled,
    K.springJpaDataEnabled,
    K.jdkInfo,
    F.idePluginState
  ],
  () => {
    const e = Do();
    F.setProjectInfoEntries(e);
  },
  { fireImmediately: !0 }
);
X.on("system-info-with-callback", (e) => {
  e.detail.callback(Po(e.detail.notify));
});
var No = Object.getOwnPropertyDescriptor, Lo = (e, t, r, n) => {
  for (var i = n > 1 ? void 0 : n ? No(t, r) : t, a = e.length - 1, o; a >= 0; a--)
    (o = e[a]) && (i = o(i) || i);
  return i;
};
let Nn = class extends _t {
  constructor() {
    super(...arguments), this.renderDialog = () => x`
    <p class="text-xs text-secondary mb-3 mt-0">
      Sign in with your Vaadin account to access all Copilot features, including:
    </p>
    <ul class="gap-3 grid grid-cols-3 list-none m-0 pb-3 ps-0 w-4xl">
      <li class="bg-violet-3 dark:bg-violet-5 flex flex-col gap-6 pb-4 pt-6 px-5 rounded-md">
        <svg
          class="max-h-10"
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 960 641.453"
          role="img"
          artist="Katerina Limpitsouni"
          source="https://undraw.co/">
          <g transform="translate(-841.956 -428.663)">
            <rect width="596" height="596" transform="translate(1205.956 474.116)" fill="#fff" />
            <path
              d="M53.661,0H89.435V1.491H53.661Zm53.661,0H143.1V1.491H107.322Zm53.661,0h35.774V1.491H160.983Zm53.661,0h35.774V1.491H214.644ZM268.3,0h35.774V1.491H268.3Zm53.661,0H357.74V1.491H321.966Zm53.661,0H411.4V1.491H375.627Zm53.661,0h35.774V1.491H429.287Zm53.661,0h35.774V1.491H482.948Zm53.661,0h35.774V1.491H536.609ZM590.27,0h5.962V29.812h-1.491V1.491H590.27Zm4.472,47.7h1.491V83.473h-1.491Zm0,53.661h1.491v35.774h-1.491Zm0,53.661h1.491v35.774h-1.491Zm0,53.661h1.491v35.774h-1.491Zm0,53.661h1.491v35.774h-1.491Zm0,53.661h1.491v35.774h-1.491Zm0,53.661h1.491v35.774h-1.491Zm0,53.661h1.491V459.1h-1.491Zm0,53.661h1.491V512.76h-1.491Zm0,53.661h1.491v35.774h-1.491Zm0,53.661h1.491v11.925H572.383v-1.491h22.359Zm-76.02,10.434H554.5v1.491H518.722Zm-53.661,0h35.774v1.491H465.061Zm-53.661,0h35.774v1.491H411.4Zm-53.661,0h35.774v1.491H357.74Zm-53.661,0h35.774v1.491H304.079Zm-53.661,0h35.774v1.491H250.418Zm-53.661,0h35.774v1.491H196.757Zm-53.661,0H178.87v1.491H143.1Zm-53.661,0h35.774v1.491H89.435Zm-53.661,0H71.548v1.491H35.774ZM0,578.346H1.491v16.4h16.4v1.491H0Zm0-53.661H1.491v35.774H0Zm0-53.661H1.491V506.8H0Zm0-53.661H1.491v35.774H0ZM0,363.7H1.491v35.774H0Zm0-53.661H1.491v35.774H0ZM0,256.38H1.491v35.774H0Zm0-53.661H1.491v35.774H0Zm0-53.661H1.491v35.774H0ZM0,95.4H1.491v35.774H0ZM0,41.736H1.491V77.51H0ZM0,0H35.774V1.491H1.491V23.849H0Z"
              transform="translate(1205.724 473.883)"
              fill="#707070" />
            <path
              d="M39.774,4a4.472,4.472,0,0,1,4.472,4.472V35.3h26.83a4.472,4.472,0,0,1,0,8.943H44.246v26.83a4.472,4.472,0,0,1-8.943,0V44.246H8.472a4.472,4.472,0,1,1,0-8.943H35.3V8.472A4.472,4.472,0,0,1,39.774,4"
              transform="translate(1464.066 732.225)"
              fill="var(--violet-9)" />
            <path
              d="M63.7,546.533l24.915,3.661,26.98-74.678-29.37-15.885Z"
              transform="translate(784.499 495.814)"
              fill="#9f616a" />
            <path
              d="M151.118,536.837h0a21.623,21.623,0,0,1,.149,7.217h0a8.547,8.547,0,0,1-9.7,7.213L64.407,539.922a5.831,5.831,0,0,1-4.919-6.617l.473-3.212s-2.4-10.216,7.211-20.962c0,0,10.807,12.45,27.547,0l5.554-7.366,25.3,25.874,16.955,4.664c3.71,1.021,7.138.976,8.6,4.534Z"
              transform="translate(782.531 517.496)"
              fill="#090814" />
            <path
              d="M94.086,556.285H119.4l12.039-97.633H94.08Z"
              transform="translate(949.701 497.399)"
              fill="#9f616a" />
            <path
              d="M182.668,533.89h0a21.729,21.729,0,0,1,1.2,7.155h0a8.59,8.59,0,0,1-8.59,8.59H96.9a5.861,5.861,0,0,1-5.861-5.861v-3.263s-3.876-9.808,4.105-21.9c0,0,9.921,9.465,24.744-5.366l4.372-7.921L155.9,528.473l17.541,2.158c3.838.473,7.24-.073,9.215,3.251Z"
              transform="translate(947.837 519.221)"
              fill="#090814" />
            <path
              d="M16.9,90.494V65.107a35.093,35.093,0,1,1,35.245.667c1.589,14.833.341,33.245.341,33.245Z"
              transform="translate(965.657 439.744)"
              fill="#9f616a" />
            <path
              d="M409.292,442.046s-7.784,93.583-9.455,113.637a142.614,142.614,0,0,1-8.355,36.764,34.568,34.568,0,0,0-3.342,13.369s-21.4,35.947-34.86,62.255a188.838,188.838,0,0,0-17.3,50.948s33.139,6.112,38.159,4.442,11.477-38.08,67.485-129.342l40.107-83.556s23.4,75.2,28.409,83.556c0,0,8.748,123.462,12.09,131.818s45.378-2.476,45.378-2.476l-7.335-126-15.04-142.045-76.871-23.4Z"
              transform="translate(516.199 287.857)"
              fill="#090814" />
            <path
              d="M24.592,220.713s3.344-11.7,0-13.371,8.357-5.009,5.015-13.367S36.291,140.5,36.291,140.5L33.733,97.013l-2.814,4.221L0,98.73S8.358,61.97,10.028,50.269c1.339-9.382,14.488-18.759,19.655-22.113l-.076-1.293,44.1-11.7c1.626.1,2.921-3.739,4.166-7.583S80.308-.092,81.73,0c13.459.867,22.424,1.435,28.4,1.808,11.9.741,13.061,16.457,13.061,16.457l41.778,25.31-.124.534A81.393,81.393,0,0,1,186.69,85.357c5.012,26.74,0,25.085,0,25.085l-33.423,16.692-5.5-10.571-4.529,10.568c10.028,30.081,13.546,74.442,11.7,80.213l6.683,21.724a169.721,169.721,0,0,1-56.113,9.322C59.069,238.39,24.592,220.713,24.592,220.713Z"
              transform="translate(899.799 514.203)"
              fill="#e6e6e6" />
            <path
              d="M524.97,341.55s-15.04,63.5,3.342,63.5,61.831-71.858,61.831-71.858l-15.034-18.377-26.994,37.479-1.413-19.1Z"
              transform="translate(539.191 273.15)"
              fill="#9f616a" />
            <circle cx="14.632" cy="14.632" r="14.632" transform="translate(1110.694 579.842)" fill="#9f616a" />
            <path
              d="M382.97,332.549s-15.04,63.5,3.342,63.5,61.831-71.858,61.831-71.858l-15.034-18.377-14.5,20.123-12.5,17.352-1.413-19.1Z"
              transform="translate(521.368 272.02)"
              fill="#9f616a" />
            <circle cx="14.632" cy="14.632" r="14.632" transform="translate(950.873 569.712)" fill="#9f616a" />
            <path
              d="M23.849,0H350.985a23.849,23.849,0,0,1,23.849,23.849V186.995a23.849,23.849,0,0,1-23.849,23.849H23.849A23.849,23.849,0,0,1,0,186.995V23.849A23.849,23.849,0,0,1,23.849,0Z"
              transform="matrix(0.998, 0.07, -0.07, 0.998, 865.395, 576.763)"
              fill="var(--violet-9)" />
            <path
              d="M216.737,47.078l4.435,5.377,8.022-14.037s10.239.527,10.239-7.072,14.123-7.39,14.123-7.39,11.673-8.73,6.929-13.109S248.731,3.3,234.58,6.44c0,0-15.848-10.856-25.989-4.245a12.435,12.435,0,0,0-2.611,2.347s-29.128,14.662-20.786,40.2l13.847,26.323,3.142-5.954s-1.9-25.011,14.563-18.043Z"
              transform="translate(774.225 428.603)"
              fill="#090814" />
          </g>
        </svg>
        <div class="flex flex-col gap-0.5">
          <span class="font-bold">Drag and drop components</span>
          <span class="text-secondary text-xs"
            >Build layouts faster by dragging components directly onto the canvas</span
          >
        </div>
      </li>
      <li class="bg-blue-3 dark:bg-blue-5 flex flex-col gap-6 pb-4 pt-6 px-5 rounded-md">
        <svg
          class="max-h-10"
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 710.40985 647.95797"
          role="img"
          artist="Katerina Limpitsouni"
          source="https://undraw.co/">
          <path
            d="M166.1564,117.90546c8.3487-46.78787,53.04582-77.94905,99.83369-69.60035,46.78787,8.3487,77.94937,53.04588,69.60067,99.83375-6.77085,37.94529-37.45011,65.60766-73.76176,70.25683l-35.69028,105.31194-71.07457-84.47181s22.13556-19.75542,36.43036-43.93366c-20.15394-19.25054-30.60088-47.90307-25.33811-77.3967Z"
            fill="#ed9da0" />
          <path
            d="M173.09718,214.78809s-113.68733,19.16988-128.68733,42.16988-31,56-31,56c0,0-43,214,25,244l274.68733-39.16988-33.68733-100.83012,34,101,79.53386-12.73159s-47.53386-235.26841-74.53386-251.26841c-27-16-60.75109-11.70056-60.75109-11.70056,0,0-53.74763,53.40351-83.99827-27.44796l-.5633-.02135Z"
            fill="var(--blue-9)" />
          <path
            d="M234.35198,83.56597s24.05787,7.39199,34.05787-4.60801c0,0,24,1,29,14s35,19,45,8c10-11,25-31,6-49,0,0-3-19-19-23l-9,8s-118.87599-55.90628-132.93799,1.04686c0,0-21.52687-4.05801-11.29444,8.94756,0,0-11.52977,13.36898-18.84417,38.72114-11.47969,39.78928,1.95017,82.60309,33.7639,109.11453l.00004.00003s-25.56332-67.10233-3.62533-47.46622,20.93799-25.3639,20.93799-25.3639c0,0,32.88426-19.78398,25.94213-38.39199Z"
            fill="#090814" />
          <path
            d="M695.55497,121.21685l-327.01201,27.71288c-6.91943.58639-12.30538,6.25775-12.53403,13.19822l-9.64485,292.76846c-.23689,7.19081,5.12962,13.33942,12.28642,14.07702l336.65687,34.69645c8.07792.83252,15.10249-5.50529,15.10249-13.62599V134.8661c0-8.02162-6.86192-14.32661-14.85488-13.64924Z"
            fill="#d6d6e3" />
          <polygon
            points="383.74945 177.01197 375.40985 437.24241 669.40985 463.95797 676.40985 156.95797 383.74945 177.01197"
            fill="#fff" />
          <path
            d="M398.944,210.64706l255.27397-14.34123c3.36201-.18888,6.19188,2.48647,6.19188,5.85378v50.364c0,3.15452-2.49595,5.74359-5.64835,5.85908l-255.27397,9.35261c-3.32.12164-6.07768-2.53686-6.07768-5.85908v-45.37537c0-3.11029,2.42876-5.67932,5.53415-5.85378Z"
            fill="#d6d6e3" />
          <path
            d="M578.40822,226.95801c-1.05908,0-1.94238-.83105-1.99609-1.90039-.05518-1.10254.79443-2.04199,1.89746-2.09668l60-3c1.11523-.05957,2.04248.79395,2.09766,1.89746.05518,1.10254-.79443,2.04199-1.89746,2.09668l-60,3c-.03418.00195-.06787.00293-.10156.00293Z"
            fill="#090814" />
          <path
            d="M548.40822,239.95801c-1.07373,0-1.96143-.85254-1.99756-1.93359-.03662-1.10352.82861-2.02832,1.93262-2.06543l90-2.99512c1.10303-.02344,2.02881.8291,2.06543,1.93262s-.82861,2.02832-1.93262,2.06543l-90,2.99512c-.02246.00098-.04492.00098-.06787.00098Z"
            fill="#090814" />
          <path
            d="M406.55761,241.5352l-7.5306,84.49779c-.33438,3.75189,2.56743,7.00734,6.33293,7.10472l102.89288,2.66102c3.45674.0894,6.38342-2.53245,6.67325-5.97817l7.57153-90.01708c.48254-5.73687-4.20762-10.5811-9.95709-10.28418l-98.8071,5.1028c-3.78001.19522-6.83981,3.14299-7.17581,6.9131Z"
            fill="#fff" />
          <path
            d="M513.04262,233.50635c1.54932,0,2.98352.63184,4.03845,1.7793,1.0531,1.14551,1.56104,2.63086,1.43054,4.18262l-7.57153,90.01709c-.10913,1.29883-1.21423,2.31592-2.51587,2.31592l-.06799-.00098-102.89307-2.66113c-.95117-.0249-1.54932-.54004-1.82104-.84473-.27197-.30518-.71533-.9585-.63086-1.90625l7.53064-84.49805c.15808-1.77441,1.6189-3.18164,3.39783-3.27344l98.80713-5.10254c.09924-.00488.19763-.00781.29578-.00781M513.04262,229.50635c-.1665,0-.33362.00439-.50208.01318l-98.80713,5.10254c-3.78003.19531-6.83984,3.14307-7.17578,6.91309l-7.53064,84.49805c-.33435,3.75146,2.5675,7.00732,6.33301,7.10449l102.89282,2.66113c.05713.00146.1145.00244.17139.00244,3.38184,0,6.2168-2.59229,6.50183-5.98047l7.57153-90.01709c.46838-5.56934-3.93774-10.29736-9.45496-10.29736h0Z"
            fill="#090814" />
          <polygon
            points="403.40985 313.95797 436.40985 272.95797 456.40985 297.95797 481.40985 277.95797 513.40985 317.95797 511.40985 332.95797 402.09718 328.78809 403.40985 313.95797"
            fill="#090814" />
          <path
            d="M554.40985,290.94489v60.53594c0,6.73742,5.46176,12.19917,12.19917,12.19917h77.75183c6.73742,0,12.19917-5.46176,12.19917-12.19917v-61.90072c0-6.82119-5.59314-12.31701-12.41327-12.19729l-77.75183,1.36478c-6.65293.11678-11.98507,5.54334-11.98507,12.19729Z"
            fill="#d6d6e3" />
          <path
            d="M615.41115,388.95801c-.02783,0-.05566-.00098-.08398-.00195l-104-4.28711c-1.10352-.04492-1.96143-.97656-1.91602-2.08008.0459-1.10449.97705-1.9541,2.08105-1.91602l104,4.28711c1.10352.04492,1.96143.97656,1.91602,2.08008-.04443,1.07617-.93066,1.91797-1.99707,1.91797Z"
            fill="#090814" />
          <path
            d="M638.41115,411.25879c-.0332,0-.06689-.00098-.10107-.00293l-127-6.33008c-1.10303-.05469-1.95312-.99316-1.89795-2.09668.05469-1.10352.97705-1.96191,2.09717-1.89746l127,6.33008c1.10303.05469,1.95312.99316,1.89795,2.09668-.05322,1.06934-.93701,1.90039-1.99609,1.90039Z"
            fill="#090814" />
          <path
            d="M621.41164,439.95801c-.04346,0-.08691-.00098-.13086-.00391l-124-8c-1.10205-.07129-1.93799-1.02246-1.86719-2.125.07129-1.10254,1.02686-1.92969,2.125-1.86719l124,8c1.10205.07129,1.93799,1.02246,1.86719,2.125-.06836,1.05859-.94824,1.87109-1.99414,1.87109Z"
            fill="#090814" />
          <circle cx="465.90985" cy="264.45797" r="8.5" fill="#090814" />
          <ellipse cx="396.40985" cy="187.95797" rx="3" ry="4" fill="#d6d6e3" />
          <ellipse cx="410.40985" cy="186.95797" rx="3" ry="4" fill="#d6d6e3" />
          <ellipse cx="422.40985" cy="185.95797" rx="3" ry="4" fill="#d6d6e3" />
          <path
            d="M678.81251,27c1.98352,0,3.59729,1.62109,3.59729,3.61377v63.37988c0,1.88428-1.41528,3.43115-3.29199,3.59814l-85.99927,7.66309c-.11133.01025-.22168.01514-.33118.01514-1.93237,0-3.54761-1.57666-3.60071-3.51416l-1.59314-58.15137c-.05005-1.82812,1.27612-3.40674,3.08521-3.67285l87.59277-12.8916c.1803-.02686.3623-.04004.54102-.04004M678.81251,23c-.37024,0-.74512.02686-1.12378.08252l-87.59241,12.8916c-3.81445.56104-6.60693,3.88574-6.50134,7.73975l1.59314,58.15137c.11389,4.15527,3.5249,7.40479,7.59924,7.40479.22705,0,.45557-.01025.68628-.03076l85.99915-7.66309c3.927-.3501,6.93701-3.64014,6.93701-7.58252V30.61377c0-4.2627-3.474-7.61377-7.59729-7.61377h0Z"
            fill="#d6d6e3" />
          <path
            d="M606.40724,61.95801c-.99756,0-1.86084-.74512-1.98291-1.76074-.13281-1.09668.64893-2.09277,1.74561-2.22461l58-7c1.09326-.13477,2.09326.64844,2.2251,1.74609.13281,1.09668-.64893,2.09277-1.74561,2.22461l-58,7c-.08154.00977-.16211.01465-.24219.01465Z"
            fill="#d6d6e3" />
          <path
            d="M606.40724,78.90039c-1.01904,0-1.89014-.77539-1.98877-1.81055-.10449-1.09961.70215-2.07617,1.80176-2.18066l52-4.94238c1.09717-.09863,2.07617.70215,2.18066,1.80176s-.70215,2.07617-1.80176,2.18066l-52,4.94238c-.06445.00586-.12842.00879-.19189.00879Z"
            fill="#d6d6e3" />
          <path
            d="M524.49989,4c1.98352,0,3.59729,1.62109,3.59729,3.61377v63.37988c0,1.88428-1.41528,3.43115-3.29199,3.59814l-85.99927,7.66309c-.11133.01025-.22168.01514-.33118.01514-1.93237,0-3.54761-1.57666-3.60071-3.51416l-1.59314-58.15137c-.05005-1.82812,1.27612-3.40674,3.08521-3.67285l87.59277-12.8916c.1803-.02686.3623-.04004.54102-.04004M524.49989,0c-.37024,0-.74512.02686-1.12378.08252l-87.59241,12.8916c-3.81445.56104-6.60693,3.88574-6.50134,7.73975l1.59314,58.15137c.11389,4.15527,3.5249,7.40479,7.59924,7.40479.22705,0,.45557-.01025.68628-.03076l85.99915-7.66309c3.927-.3501,6.93701-3.64014,6.93701-7.58252V7.61377c0-4.2627-3.474-7.61377-7.59729-7.61377h0Z"
            fill="#d6d6e3" />
          <path
            d="M452.09462,38.95801c-.99756,0-1.86084-.74512-1.98291-1.76074-.13281-1.09668.64893-2.09277,1.74561-2.22461l58-7c1.09326-.13477,2.09326.64844,2.2251,1.74609.13281,1.09668-.64893,2.09277-1.74561,2.22461l-58,7c-.08154.00977-.16211.01465-.24219.01465Z"
            fill="#d6d6e3" />
          <path
            d="M452.09462,55.90039c-1.01904,0-1.89014-.77539-1.98877-1.81055-.10449-1.09961.70215-2.07617,1.80176-2.18066l52-4.94238c1.09717-.09863,2.07617.70215,2.18066,1.80176s-.70215,2.07617-1.80176,2.18066l-52,4.94238c-.06445.00586-.12842.00879-.19189.00879Z"
            fill="#d6d6e3" />
          <polygon
            points="164.06416 583.34819 372.40985 647.95797 554.40985 585.84979 348.40985 544.95797 164.06416 583.34819"
            fill="#d6d6e3" />
          <polygon
            points="315.40985 561.95797 352.40985 552.95797 436.57461 571.46559 398.40985 583.34819 315.40985 561.95797"
            fill="#fff" />
          <polygon
            points="402.40985 590.85298 323.40985 614.95797 378.40985 632.95797 453.40985 604.95797 402.40985 590.85298"
            fill="#fff" />
          <path
            d="M342.82368,436.24076l-167.32222,89.15785-32.17034-47.43578,174.86391-87.70758c3.64657-12.7663,13.25103-24.84968,27.28203-32.36433,24.55064-13.14867,53.27389-7.33701,64.1556,12.98075,10.88163,20.3178-.19906,47.44737-24.74977,60.59608-14.03108,7.51469-29.41198,8.81231-42.05921,4.77302Z"
            fill="#ed9da0" />
          <path
            d="M108.40985,396.95797l-8,24s19,20.28725,0,31.64363l67-8.64363s27.12401-20.00542,29.06201-3.50271c0,0,33.93799-22.49729,40.93799-8.49729,0,0-20.07249,43.40586,11.46376,65.70293l-41.96376,18.86878-29.89001,20.22884-138.60999,20.19945"
            fill="var(--blue-9)" />
          <path
            d="M464.7671,425.68457c-.35059,0-.70361-.0166-1.05908-.05078l-57.14062-5.48438c-5.97754-.57324-10.46094-5.91113-9.99463-11.89844l3.68311-47.25195c.47168-6.04199,5.78662-10.5957,11.81836-10.19922l58.62061,4.0498c3.03174.20996,5.77734,1.60742,7.72998,3.93555,1.95312,2.32812,2.85205,5.27344,2.53125,8.29492l-5.16357,48.68652c-.60059,5.66406-5.43652,9.91797-11.02539,9.91797ZM402.74855,361.19336l2.49268.19434-3.68311,47.25195c-.25635,3.28711,2.20508,6.21777,5.48682,6.5332l57.14062,5.48438c3.31152.31934,6.28467-2.11133,6.63525-5.41797l5.16357-48.68652c.17578-1.65918-.31787-3.27637-1.39014-4.55371-1.07227-1.27832-2.57959-2.0459-4.24414-2.16113l-58.62061-4.0498c-3.31592-.21973-6.22949,2.28125-6.48828,5.59961l-2.49268-.19434Z"
            fill="var(--blue-9)" />
          <polygon
            points="402.40985 412.95797 424.40985 390.95797 431.40985 400.95797 448.40985 381.95797 470.73937 412.95797 469.40985 423.95797 403.40985 418.80504 402.40985 412.95797"
            fill="var(--blue-9)" />
        </svg>
        <div class="flex flex-col gap-0.5">
          <span class="font-bold">Duplicate and copy components</span>
          <span class="text-secondary text-xs"
            >Quickly reuse elements by duplicating or copying them across your project</span
          >
        </div>
      </li>
      <li class="bg-teal-3 dark:bg-teal-5 flex flex-col gap-6 pb-4 pt-6 px-5 rounded-md">
        <svg
          class="max-h-10"
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 799.031 618.112"
          role="img"
          artist="Katerina Limpitsouni"
          source="https://undraw.co/">
          <g transform="translate(-560.484 -230.944)">
            <path
              d="M15.18,488.763c0,.872.478,1.573,1.073,1.573h535.1c.6,0,1.073-.7,1.073-1.573s-.478-1.573-1.073-1.573H16.253C15.658,487.191,15.18,487.891,15.18,488.763Z"
              transform="translate(675.195 358.72)"
              fill="#ccc" />
            <rect width="19.105" height="3.371" transform="translate(865.646 842.298)" fill="#b6b3c5" />
            <rect width="19.105" height="3.371" transform="translate(1034.779 842.861)" fill="#b6b3c5" />
            <path
              d="M352.955,370.945a27.529,27.529,0,0,1-54.321,0H229.146V521.536h193.3V370.945Z"
              transform="translate(634.205 321.322)"
              fill="#d6d6e3" />
            <rect width="193.296" height="5.242" transform="translate(863.914 830.927)" fill="#090814" />
            <path
              d="M788.255,487.17H10.776A10.788,10.788,0,0,1,0,476.394V32.688A10.788,10.788,0,0,1,10.776,21.911H788.255a10.789,10.789,0,0,1,10.776,10.776V476.394a10.789,10.789,0,0,1-10.776,10.776Z"
              transform="translate(560.484 209.033)"
              fill="#090814" />
            <rect width="760.822" height="429.297" transform="translate(578.588 248)" fill="#fff" />
            <g transform="translate(0 -41.857)">
              <g transform="translate(-588.477 33.946)">
                <path
                  d="M35.524,67.628A24.524,24.524,0,0,1,11,43.1V36.524A24.524,24.524,0,0,1,35.524,12a1.492,1.492,0,1,1,0,2.983,21.54,21.54,0,0,0-21.54,21.54V43.1a21.54,21.54,0,1,0,43.081,0V31.259a1.492,1.492,0,1,1,2.983,0V43.1A24.524,24.524,0,0,1,35.524,67.628Z"
                  transform="translate(1535.985 422.718)" />
                <path
                  d="M28.524,67.628A24.524,24.524,0,0,1,4,43.1V31.259a1.492,1.492,0,1,1,2.983,0V43.1a21.54,21.54,0,1,0,43.081,0V36.524a21.54,21.54,0,0,0-21.54-21.54,1.492,1.492,0,0,1,0-2.983A24.524,24.524,0,0,1,53.047,36.524V43.1A24.524,24.524,0,0,1,28.524,67.628Z"
                  transform="translate(1496.922 422.718)" />
                <path
                  d="M58.556,46.441a1.492,1.492,0,0,1-1.492-1.492V26.524a21.54,21.54,0,1,0-43.081,0,1.492,1.492,0,1,1-2.983,0,24.524,24.524,0,1,1,49.047,0V44.949A1.492,1.492,0,0,1,58.556,46.441Z"
                  transform="translate(1535.985 366.911)" />
                <path
                  d="M51.556,93.821a1.492,1.492,0,0,1-1.492-1.492V26.524a21.54,21.54,0,1,0-43.081,0V44.949a1.492,1.492,0,0,1-2.983,0V26.524A24.524,24.524,0,0,1,45.864,9.183a24.363,24.363,0,0,1,7.183,17.341V92.329A1.492,1.492,0,0,1,51.556,93.821Z"
                  transform="translate(1496.922 366.911)" />
                <g transform="translate(1570.017 382.073)">
                  <path
                    d="M20.782,57.047a1.492,1.492,0,1,1,0-2.983,21.54,21.54,0,1,0,0-43.081h-3.29a1.492,1.492,0,0,1,0-2.983h3.29a24.524,24.524,0,1,1,0,49.047Z"
                    transform="translate(-10.602 18.322)" />
                  <path
                    d="M19.372,37.305a1.492,1.492,0,1,1,0-2.983,11.67,11.67,0,1,0,0-23.339h-1.88a1.492,1.492,0,0,1,0-2.983h1.88a14.653,14.653,0,1,1,0,29.305Z"
                    transform="translate(-16 -8)" />
                  <path
                    d="M19.372,37.305h-1.88a1.492,1.492,0,1,1,0-2.983h1.88a11.67,11.67,0,0,0,0-23.339,1.492,1.492,0,0,1,0-2.983,14.653,14.653,0,0,1,0,29.305Z"
                    transform="translate(-16 62.894)" />
                </g>
                <g transform="translate(1492.234 382.073)">
                  <path
                    d="M40.523,57.047A24.524,24.524,0,0,1,40.523,8h3.29a1.492,1.492,0,1,1,0,2.983h-3.29a21.54,21.54,0,0,0,0,43.081,1.492,1.492,0,0,1,0,2.983Z"
                    transform="translate(-16 18.322)" />
                  <path
                    d="M30.652,37.305A14.653,14.653,0,0,1,30.652,8h1.88a1.492,1.492,0,1,1,0,2.983h-1.88a11.67,11.67,0,0,0,0,23.339,1.492,1.492,0,0,1,0,2.983Z"
                    transform="translate(0.678 -8)" />
                  <path
                    d="M32.532,37.305h-1.88A14.653,14.653,0,0,1,30.652,8a1.492,1.492,0,0,1,0,2.983,11.67,11.67,0,0,0,0,23.339h1.88a1.492,1.492,0,1,1,0,2.983Z"
                    transform="translate(0.679 62.894)" />
                </g>
              </g>
              <g transform="translate(864.012 553.398)">
                <rect width="29.619" height="7.13" rx="3.565" transform="translate(37.298)" fill="var(--teal-9)" />
                <rect width="10.421" height="7.13" rx="3.565" transform="translate(159.064)" fill="var(--teal-9)" />
                <rect width="10.421" height="7.13" rx="3.565" transform="translate(179.908)" fill="var(--teal-9)" />
                <rect width="70.756" height="7.13" rx="3.565" transform="translate(77.338)" fill="var(--teal-9)" />
                <rect
                  width="29.619"
                  height="7.13"
                  rx="3.565"
                  transform="translate(0.001 46.074)"
                  fill="var(--teal-9)" />
                <rect
                  width="10.421"
                  height="7.13"
                  rx="3.565"
                  transform="translate(121.767 46.074)"
                  fill="var(--teal-9)" />
                <rect
                  width="10.421"
                  height="7.13"
                  rx="3.565"
                  transform="translate(142.61 46.074)"
                  fill="var(--teal-9)" />
                <rect
                  width="70.756"
                  height="7.13"
                  rx="3.565"
                  transform="translate(40.041 46.074)"
                  fill="var(--teal-9)" />
                <rect
                  width="29.619"
                  height="7.13"
                  rx="3.565"
                  transform="translate(122.316 15.906)"
                  fill="var(--teal-9)" />
                <rect
                  width="29.619"
                  height="7.13"
                  rx="3.565"
                  transform="translate(0.001 15.906)"
                  fill="var(--teal-9)" />
                <rect width="10.421" height="7.13" rx="3.565" transform="translate(0.001)" fill="var(--teal-9)" />
                <rect width="10.421" height="7.13" rx="3.565" transform="translate(0 31.264)" fill="var(--teal-9)" />
                <rect
                  width="70.756"
                  height="7.13"
                  rx="3.565"
                  transform="translate(41.686 15.906)"
                  fill="var(--teal-9)" />
                <rect
                  width="29.619"
                  height="7.13"
                  rx="3.565"
                  transform="translate(60.884 31.264)"
                  fill="var(--teal-9)" />
                <rect
                  width="29.619"
                  height="7.13"
                  rx="3.565"
                  transform="translate(20.843 31.264)"
                  fill="var(--teal-9)" />
                <rect width="10.421" height="7.13" rx="3.565" transform="translate(18.675)" fill="var(--teal-9)" />
                <rect
                  width="10.421"
                  height="7.13"
                  rx="3.565"
                  transform="translate(181.553 31.264)"
                  fill="var(--teal-9)" />
                <rect
                  width="70.756"
                  height="7.13"
                  rx="3.565"
                  transform="translate(100.375 31.264)"
                  fill="var(--teal-9)" />
              </g>
            </g>
            <g transform="translate(626.555 602.469)">
              <path
                d="M805.134,330.7H727.95a1.546,1.546,0,0,1-1.544-1.544V314.612h.618V329.16a.928.928,0,0,0,.927.927h77.184a.928.928,0,0,0,.927-.927V314.51h.618V329.16A1.546,1.546,0,0,1,805.134,330.7Z"
                transform="translate(-646.44 -292.702)"
                fill="#3f3d56" />
              <rect width="181.374" height="0.618" transform="translate(5.3 21.601)" fill="#3f3d56" />
              <ellipse
                cx="5.313"
                cy="5.313"
                rx="5.313"
                ry="5.313"
                transform="translate(0.001 16.549)"
                fill="var(--teal-9)" />
              <ellipse
                cx="5.313"
                cy="5.313"
                rx="5.313"
                ry="5.313"
                transform="translate(53.991 16.549)"
                fill="var(--teal-9)" />
              <ellipse
                cx="5.313"
                cy="5.313"
                rx="5.313"
                ry="5.313"
                transform="translate(90.634 32.165)"
                fill="#3f3d56" />
              <ellipse cx="5.313" cy="5.313" rx="5.313" ry="5.313" transform="translate(118.489 32.165)" fill="#ccc" />
              <ellipse
                cx="5.313"
                cy="5.313"
                rx="5.313"
                ry="5.313"
                transform="translate(104.991 16.549)"
                fill="var(--teal-9)" />
              <ellipse
                cx="5.313"
                cy="5.313"
                rx="5.313"
                ry="5.313"
                transform="translate(180.632 16.549)"
                fill="var(--teal-9)" />
              <ellipse
                cx="5.313"
                cy="5.313"
                rx="5.313"
                ry="5.313"
                transform="translate(154.616 16.549)"
                fill="var(--teal-9)" />
              <path
                d="M537.36,277.577a.309.309,0,0,1-.309-.309V262.022a1.546,1.546,0,0,1,1.544-1.544H553.63a.309.309,0,1,1,0,.618H538.6a.928.928,0,0,0-.927.927v15.246a.309.309,0,0,1-.309.309Z"
                transform="translate(-515.571 -255.358)"
                fill="#3f3d56" />
              <ellipse cx="5.313" cy="5.313" rx="5.313" ry="5.313" transform="translate(33.452 0)" fill="#e6e6e6" />
              <path
                d="M921.669,277.268h-.618V262.022a1.546,1.546,0,0,1,1.544-1.544H937.63v.618H922.6a.928.928,0,0,0-.927.927Z"
                transform="translate(-780.967 -255.358)"
                fill="#3f3d56" />
              <ellipse cx="5.313" cy="5.313" rx="5.313" ry="5.313" transform="translate(152.058 0)" fill="#e6e6e6" />
            </g>
            <path
              d="M496.375,205.477c-2.221,0-4.027.792-4.027,1.764v1.411c0,.973,1.806,1.764,4.027,1.764h93.434c2.221,0,4.027-.792,4.027-1.764v-1.411c0-.973-1.806-1.764-4.027-1.764Z"
              transform="translate(635.637 363.33)"
              fill="#f2f2f2" />
            <path
              d="M670.026,309.282c4,0,7.249,1.75,7.249,3.9v30.351c0,2.152-3.252,3.9-7.249,3.9H497.656c-4,0-7.249-1.75-7.249-3.9V313.184c0-2.152,3.252-3.9,7.249-3.9"
              transform="translate(637.578 297.505)"
              fill="#f2f2f2" />
            <path
              d="M496.375,234.581c-2.221,0-4.027.973-4.027,2.168s1.806,2.168,4.027,2.168H639.748c2.221,0,4.027-.973,4.027-2.168s-1.806-2.168-4.027-2.168Z"
              transform="translate(635.637 343.828)"
              fill="#f2f2f2" />
            <path
              d="M496.375,234.581c-2.221,0-4.027.973-4.027,2.168s1.806,2.168,4.027,2.168H639.748c2.221,0,4.027-.973,4.027-2.168s-1.806-2.168-4.027-2.168Z"
              transform="translate(635.637 352.828)"
              fill="#f2f2f2" />
            <path
              d="M891.9,191.277H840.311a1.683,1.683,0,1,1,0-3.367H891.9a1.683,1.683,0,1,1,0,3.367Z"
              transform="translate(-212.074 93.872)"
              fill="var(--teal-9)" />
            <path
              d="M862.672,210.649H840.311a1.683,1.683,0,1,1,0-3.367h22.361a1.683,1.683,0,1,1,0,3.367Z"
              transform="translate(-212.074 81.146)"
              fill="var(--teal-9)" />
            <g transform="translate(690.275 280.103)">
              <ellipse cx="6.686" cy="6.686" rx="6.686" ry="6.686" transform="translate(0 0)" fill="var(--teal-9)" />
              <path
                d="M847.243,585.331H847.2a.874.874,0,0,1-.646-.336l-1.118-1.434a.875.875,0,0,1,.154-1.228l.04-.032a.874.874,0,0,1,1.228.154.638.638,0,0,0,.966.047l2.267-2.4a.876.876,0,0,1,1.237-.034l.037.035a.874.874,0,0,1,.034,1.237l-3.521,3.716a.874.874,0,0,1-.635.273Z"
                transform="translate(-841.667 -576.242)"
                fill="#fff" />
            </g>
            <path
              d="M891.9,191.277H840.311a1.683,1.683,0,1,1,0-3.367H891.9a1.683,1.683,0,1,1,0,3.367Z"
              transform="translate(-212.074 129.452)"
              fill="var(--teal-9)" />
            <path
              d="M862.672,210.649H840.311a1.683,1.683,0,1,1,0-3.367h22.361a1.683,1.683,0,1,1,0,3.367Z"
              transform="translate(-212.074 116.727)"
              fill="var(--teal-9)" />
            <g transform="translate(690.275 315.683)">
              <ellipse cx="6.686" cy="6.686" rx="6.686" ry="6.686" transform="translate(0 0)" fill="#e6e6e6" />
              <path
                d="M847.243,585.331H847.2a.874.874,0,0,1-.646-.336l-1.118-1.434a.875.875,0,0,1,.154-1.228l.04-.032a.874.874,0,0,1,1.228.154.638.638,0,0,0,.966.047l2.267-2.4a.876.876,0,0,1,1.237-.034l.037.035a.874.874,0,0,1,.034,1.237l-3.521,3.716a.874.874,0,0,1-.635.273Z"
                transform="translate(-841.667 -576.242)"
                fill="#fff" />
            </g>
            <path
              d="M891.9,191.277H840.311a1.683,1.683,0,1,1,0-3.367H891.9a1.683,1.683,0,1,1,0,3.367Z"
              transform="translate(-212.074 165.032)"
              fill="var(--teal-9)" />
            <path
              d="M862.672,210.649H840.311a1.683,1.683,0,1,1,0-3.367h22.361a1.683,1.683,0,1,1,0,3.367Z"
              transform="translate(-212.074 152.307)"
              fill="var(--teal-9)" />
            <g transform="translate(690.275 351.262)">
              <ellipse cx="6.686" cy="6.686" rx="6.686" ry="6.686" transform="translate(0 0)" fill="#e6e6e6" />
              <path
                d="M847.243,585.331H847.2a.874.874,0,0,1-.646-.336l-1.118-1.434a.875.875,0,0,1,.154-1.228l.04-.032a.874.874,0,0,1,1.228.154.638.638,0,0,0,.966.047l2.267-2.4a.876.876,0,0,1,1.237-.034l.037.035a.874.874,0,0,1,.034,1.237l-3.521,3.716a.874.874,0,0,1-.635.273Z"
                transform="translate(-841.667 -576.242)"
                fill="#fff" />
            </g>
            <path
              d="M891.9,191.277H840.311a1.683,1.683,0,1,1,0-3.367H891.9a1.683,1.683,0,1,1,0,3.367Z"
              transform="translate(-212.074 200.611)"
              fill="#e6e6e6" />
            <path
              d="M862.672,210.649H840.311a1.683,1.683,0,1,1,0-3.367h22.361a1.683,1.683,0,1,1,0,3.367Z"
              transform="translate(-212.074 187.886)"
              fill="#e6e6e6" />
            <g transform="translate(690.275 386.842)">
              <ellipse cx="6.686" cy="6.686" rx="6.686" ry="6.686" transform="translate(0 0)" fill="#e6e6e6" />
              <path
                d="M847.243,585.331H847.2a.874.874,0,0,1-.646-.336l-1.118-1.434a.875.875,0,0,1,.154-1.228l.04-.032a.874.874,0,0,1,1.228.154.638.638,0,0,0,.966.047l2.267-2.4a.876.876,0,0,1,1.237-.034l.037.035a.874.874,0,0,1,.034,1.237l-3.521,3.716a.874.874,0,0,1-.635.273Z"
                transform="translate(-841.667 -576.242)"
                fill="#fff" />
            </g>
            <path
              d="M891.9,191.277H840.311a1.683,1.683,0,1,1,0-3.367H891.9a1.683,1.683,0,1,1,0,3.367Z"
              transform="translate(-212.074 236.191)"
              fill="#e6e6e6" />
            <path
              d="M862.672,210.649H840.311a1.683,1.683,0,1,1,0-3.367h22.361a1.683,1.683,0,1,1,0,3.367Z"
              transform="translate(-212.074 223.466)"
              fill="#e6e6e6" />
            <g transform="translate(690.275 422.422)">
              <ellipse cx="6.686" cy="6.686" rx="6.686" ry="6.686" transform="translate(0 0)" fill="#e6e6e6" />
              <path
                d="M847.243,585.331H847.2a.874.874,0,0,1-.646-.336l-1.118-1.434a.875.875,0,0,1,.154-1.228l.04-.032a.874.874,0,0,1,1.228.154.638.638,0,0,0,.966.047l2.267-2.4a.876.876,0,0,1,1.237-.034l.037.035a.874.874,0,0,1,.034,1.237l-3.521,3.716a.874.874,0,0,1-.635.273Z"
                transform="translate(-841.667 -576.242)"
                fill="#fff" />
            </g>
            <g transform="translate(587.66 -327.248)">
              <path
                d="M345.8,318H248.438a.3.3,0,0,1-.3-.3c0-2.109,97.967-.168,97.967,0A.3.3,0,0,1,345.8,318Z"
                transform="translate(381.092 338.302)"
                fill="#090814" />
              <path
                d="M290.014,369.407h-8.855a.905.905,0,0,1-.9-.9V356.3a.905.905,0,0,1,.9-.9h8.855a.905.905,0,0,1,.9.9V368.5A.905.905,0,0,1,290.014,369.407Z"
                transform="translate(360.316 283.544)"
                fill="var(--teal-9)" />
              <path
                d="M335.73,348.208h-8.855a.905.905,0,0,1-.9-.9V323.518a.905.905,0,0,1,.9-.9h8.855a.905.905,0,0,1,.9.9V347.3A.905.905,0,0,1,335.73,348.208Z"
                transform="translate(330.75 304.743)"
                fill="var(--teal-9)" />
              <path
                d="M381.445,369.407H372.59a.905.905,0,0,1-.9-.9V356.3a.905.905,0,0,1,.9-.9h8.855a.905.905,0,0,1,.9.9V368.5A.905.905,0,0,1,381.445,369.407Z"
                transform="translate(301.181 283.544)"
                fill="var(--teal-9)" />
              <path
                d="M427.161,339.839h-8.855a.886.886,0,0,1-.9-.863V310.539a.886.886,0,0,1,.9-.863h8.855a.886.886,0,0,1,.9.863v28.437A.886.886,0,0,1,427.161,339.839Z"
                transform="translate(271.615 313.112)"
                fill="var(--teal-9)" />
              <path
                d="M472.877,324.777h-8.855a.905.905,0,0,1-.9-.9V287.291a.905.905,0,0,1,.9-.9h8.855a.905.905,0,0,1,.9.9v36.581A.905.905,0,0,1,472.877,324.777Z"
                transform="translate(242.049 328.175)"
                fill="var(--teal-9)" />
            </g>
          </g>
        </svg>
        <div class="flex flex-col gap-0.5">
          <span class="font-bold">Built-in AI tools</span>
          <span class="text-secondary text-xs">Generate views from images, use the docs assistant, and more</span>
        </div>
      </li>
    </ul>
  `, this.renderFooter = () => x`
    <vaadin-button @click=${this.cancelClickListener}>Cancel</vaadin-button>
    <vaadin-button theme="primary" @click=${this.loginClickListener}>Login</vaadin-button>
  `, this.tryAcquireLicense = () => {
      const e = "vaadin-copilot", t = K.serverVersions.find(
        (r) => r.name === "Vaadin"
      )?.version;
      window.Vaadin.devTools.downloadLicense({ name: e, version: t });
    };
  }
  createRenderRoot() {
    return this;
  }
  render() {
    return x` <vaadin-dialog
      @opened-changed="${(e) => {
      const t = e.detail.value;
      F.setLoginCheckActive(t);
    }}"
      .opened="${F.loginCheckActive}"
      ${Ht(
      () => x`
          <h2 class="font-bold me-auto my-0 text-xs truncate uppercase">Unlock all AI features</h2>
          <vaadin-button aria-label="Close" @click="${() => {
      }}" theme="icon tertiary">
            <vaadin-icon .svg="${ne.close}"></vaadin-icon>
            <vaadin-tooltip slot="tooltip" text="Close"></vaadin-tooltip>
          </vaadin-button>
        `
    )}
      ${Mt(this.renderDialog, [])}
      ${Vt(this.renderFooter, [])}>
    </vaadin-dialog>`;
  }
  cancelClickListener(e) {
    F.setLoginCheckActive(!1);
  }
  loginClickListener() {
    an("license-acquired"), this.tryAcquireLicense();
  }
};
Nn = Lo([
  Ne("copilot-login-check")
], Nn);
var gt = { exports: {} }, ir, Ln;
function Ft() {
  if (Ln) return ir;
  Ln = 1;
  const e = "2.0.0", t = 256, r = Number.MAX_SAFE_INTEGER || /* istanbul ignore next */
  9007199254740991, n = 16, i = t - 6;
  return ir = {
    MAX_LENGTH: t,
    MAX_SAFE_COMPONENT_LENGTH: n,
    MAX_SAFE_BUILD_LENGTH: i,
    MAX_SAFE_INTEGER: r,
    RELEASE_TYPES: [
      "major",
      "premajor",
      "minor",
      "preminor",
      "patch",
      "prepatch",
      "prerelease"
    ],
    SEMVER_SPEC_VERSION: e,
    FLAG_INCLUDE_PRERELEASE: 1,
    FLAG_LOOSE: 2
  }, ir;
}
var ar, Mn;
function kt() {
  return Mn || (Mn = 1, ar = typeof process == "object" && process.env && process.env.NODE_DEBUG && /\bsemver\b/i.test(process.env.NODE_DEBUG) ? (...t) => console.error("SEMVER", ...t) : () => {
  }), ar;
}
var Hn;
function dt() {
  return Hn || (Hn = 1, function(e, t) {
    const {
      MAX_SAFE_COMPONENT_LENGTH: r,
      MAX_SAFE_BUILD_LENGTH: n,
      MAX_LENGTH: i
    } = Ft(), a = kt();
    t = e.exports = {};
    const o = t.re = [], d = t.safeRe = [], c = t.src = [], s = t.safeSrc = [], l = t.t = {};
    let u = 0;
    const h = "[a-zA-Z0-9-]", p = [
      ["\\s", 1],
      ["\\d", i],
      [h, n]
    ], E = ($) => {
      for (const [_, H] of p)
        $ = $.split(`${_}*`).join(`${_}{0,${H}}`).split(`${_}+`).join(`${_}{1,${H}}`);
      return $;
    }, f = ($, _, H) => {
      const D = E(_), q = u++;
      a($, q, _), l[$] = q, c[q] = _, s[q] = D, o[q] = new RegExp(_, H ? "g" : void 0), d[q] = new RegExp(D, H ? "g" : void 0);
    };
    f("NUMERICIDENTIFIER", "0|[1-9]\\d*"), f("NUMERICIDENTIFIERLOOSE", "\\d+"), f("NONNUMERICIDENTIFIER", `\\d*[a-zA-Z-]${h}*`), f("MAINVERSION", `(${c[l.NUMERICIDENTIFIER]})\\.(${c[l.NUMERICIDENTIFIER]})\\.(${c[l.NUMERICIDENTIFIER]})`), f("MAINVERSIONLOOSE", `(${c[l.NUMERICIDENTIFIERLOOSE]})\\.(${c[l.NUMERICIDENTIFIERLOOSE]})\\.(${c[l.NUMERICIDENTIFIERLOOSE]})`), f("PRERELEASEIDENTIFIER", `(?:${c[l.NONNUMERICIDENTIFIER]}|${c[l.NUMERICIDENTIFIER]})`), f("PRERELEASEIDENTIFIERLOOSE", `(?:${c[l.NONNUMERICIDENTIFIER]}|${c[l.NUMERICIDENTIFIERLOOSE]})`), f("PRERELEASE", `(?:-(${c[l.PRERELEASEIDENTIFIER]}(?:\\.${c[l.PRERELEASEIDENTIFIER]})*))`), f("PRERELEASELOOSE", `(?:-?(${c[l.PRERELEASEIDENTIFIERLOOSE]}(?:\\.${c[l.PRERELEASEIDENTIFIERLOOSE]})*))`), f("BUILDIDENTIFIER", `${h}+`), f("BUILD", `(?:\\+(${c[l.BUILDIDENTIFIER]}(?:\\.${c[l.BUILDIDENTIFIER]})*))`), f("FULLPLAIN", `v?${c[l.MAINVERSION]}${c[l.PRERELEASE]}?${c[l.BUILD]}?`), f("FULL", `^${c[l.FULLPLAIN]}$`), f("LOOSEPLAIN", `[v=\\s]*${c[l.MAINVERSIONLOOSE]}${c[l.PRERELEASELOOSE]}?${c[l.BUILD]}?`), f("LOOSE", `^${c[l.LOOSEPLAIN]}$`), f("GTLT", "((?:<|>)?=?)"), f("XRANGEIDENTIFIERLOOSE", `${c[l.NUMERICIDENTIFIERLOOSE]}|x|X|\\*`), f("XRANGEIDENTIFIER", `${c[l.NUMERICIDENTIFIER]}|x|X|\\*`), f("XRANGEPLAIN", `[v=\\s]*(${c[l.XRANGEIDENTIFIER]})(?:\\.(${c[l.XRANGEIDENTIFIER]})(?:\\.(${c[l.XRANGEIDENTIFIER]})(?:${c[l.PRERELEASE]})?${c[l.BUILD]}?)?)?`), f("XRANGEPLAINLOOSE", `[v=\\s]*(${c[l.XRANGEIDENTIFIERLOOSE]})(?:\\.(${c[l.XRANGEIDENTIFIERLOOSE]})(?:\\.(${c[l.XRANGEIDENTIFIERLOOSE]})(?:${c[l.PRERELEASELOOSE]})?${c[l.BUILD]}?)?)?`), f("XRANGE", `^${c[l.GTLT]}\\s*${c[l.XRANGEPLAIN]}$`), f("XRANGELOOSE", `^${c[l.GTLT]}\\s*${c[l.XRANGEPLAINLOOSE]}$`), f("COERCEPLAIN", `(^|[^\\d])(\\d{1,${r}})(?:\\.(\\d{1,${r}}))?(?:\\.(\\d{1,${r}}))?`), f("COERCE", `${c[l.COERCEPLAIN]}(?:$|[^\\d])`), f("COERCEFULL", c[l.COERCEPLAIN] + `(?:${c[l.PRERELEASE]})?(?:${c[l.BUILD]})?(?:$|[^\\d])`), f("COERCERTL", c[l.COERCE], !0), f("COERCERTLFULL", c[l.COERCEFULL], !0), f("LONETILDE", "(?:~>?)"), f("TILDETRIM", `(\\s*)${c[l.LONETILDE]}\\s+`, !0), t.tildeTrimReplace = "$1~", f("TILDE", `^${c[l.LONETILDE]}${c[l.XRANGEPLAIN]}$`), f("TILDELOOSE", `^${c[l.LONETILDE]}${c[l.XRANGEPLAINLOOSE]}$`), f("LONECARET", "(?:\\^)"), f("CARETTRIM", `(\\s*)${c[l.LONECARET]}\\s+`, !0), t.caretTrimReplace = "$1^", f("CARET", `^${c[l.LONECARET]}${c[l.XRANGEPLAIN]}$`), f("CARETLOOSE", `^${c[l.LONECARET]}${c[l.XRANGEPLAINLOOSE]}$`), f("COMPARATORLOOSE", `^${c[l.GTLT]}\\s*(${c[l.LOOSEPLAIN]})$|^$`), f("COMPARATOR", `^${c[l.GTLT]}\\s*(${c[l.FULLPLAIN]})$|^$`), f("COMPARATORTRIM", `(\\s*)${c[l.GTLT]}\\s*(${c[l.LOOSEPLAIN]}|${c[l.XRANGEPLAIN]})`, !0), t.comparatorTrimReplace = "$1$2$3", f("HYPHENRANGE", `^\\s*(${c[l.XRANGEPLAIN]})\\s+-\\s+(${c[l.XRANGEPLAIN]})\\s*$`), f("HYPHENRANGELOOSE", `^\\s*(${c[l.XRANGEPLAINLOOSE]})\\s+-\\s+(${c[l.XRANGEPLAINLOOSE]})\\s*$`), f("STAR", "(<|>)?=?\\s*\\*"), f("GTE0", "^\\s*>=\\s*0\\.0\\.0\\s*$"), f("GTE0PRE", "^\\s*>=\\s*0\\.0\\.0-0\\s*$");
  }(gt, gt.exports)), gt.exports;
}
var or, Vn;
function fn() {
  if (Vn) return or;
  Vn = 1;
  const e = Object.freeze({ loose: !0 }), t = Object.freeze({});
  return or = (n) => n ? typeof n != "object" ? e : n : t, or;
}
var sr, qn;
function ta() {
  if (qn) return sr;
  qn = 1;
  const e = /^[0-9]+$/, t = (n, i) => {
    if (typeof n == "number" && typeof i == "number")
      return n === i ? 0 : n < i ? -1 : 1;
    const a = e.test(n), o = e.test(i);
    return a && o && (n = +n, i = +i), n === i ? 0 : a && !o ? -1 : o && !a ? 1 : n < i ? -1 : 1;
  };
  return sr = {
    compareIdentifiers: t,
    rcompareIdentifiers: (n, i) => t(i, n)
  }, sr;
}
var lr, Fn;
function ie() {
  if (Fn) return lr;
  Fn = 1;
  const e = kt(), { MAX_LENGTH: t, MAX_SAFE_INTEGER: r } = Ft(), { safeRe: n, t: i } = dt(), a = fn(), { compareIdentifiers: o } = ta();
  class d {
    constructor(s, l) {
      if (l = a(l), s instanceof d) {
        if (s.loose === !!l.loose && s.includePrerelease === !!l.includePrerelease)
          return s;
        s = s.version;
      } else if (typeof s != "string")
        throw new TypeError(`Invalid version. Must be a string. Got type "${typeof s}".`);
      if (s.length > t)
        throw new TypeError(
          `version is longer than ${t} characters`
        );
      e("SemVer", s, l), this.options = l, this.loose = !!l.loose, this.includePrerelease = !!l.includePrerelease;
      const u = s.trim().match(l.loose ? n[i.LOOSE] : n[i.FULL]);
      if (!u)
        throw new TypeError(`Invalid Version: ${s}`);
      if (this.raw = s, this.major = +u[1], this.minor = +u[2], this.patch = +u[3], this.major > r || this.major < 0)
        throw new TypeError("Invalid major version");
      if (this.minor > r || this.minor < 0)
        throw new TypeError("Invalid minor version");
      if (this.patch > r || this.patch < 0)
        throw new TypeError("Invalid patch version");
      u[4] ? this.prerelease = u[4].split(".").map((h) => {
        if (/^[0-9]+$/.test(h)) {
          const p = +h;
          if (p >= 0 && p < r)
            return p;
        }
        return h;
      }) : this.prerelease = [], this.build = u[5] ? u[5].split(".") : [], this.format();
    }
    format() {
      return this.version = `${this.major}.${this.minor}.${this.patch}`, this.prerelease.length && (this.version += `-${this.prerelease.join(".")}`), this.version;
    }
    toString() {
      return this.version;
    }
    compare(s) {
      if (e("SemVer.compare", this.version, this.options, s), !(s instanceof d)) {
        if (typeof s == "string" && s === this.version)
          return 0;
        s = new d(s, this.options);
      }
      return s.version === this.version ? 0 : this.compareMain(s) || this.comparePre(s);
    }
    compareMain(s) {
      return s instanceof d || (s = new d(s, this.options)), this.major < s.major ? -1 : this.major > s.major ? 1 : this.minor < s.minor ? -1 : this.minor > s.minor ? 1 : this.patch < s.patch ? -1 : this.patch > s.patch ? 1 : 0;
    }
    comparePre(s) {
      if (s instanceof d || (s = new d(s, this.options)), this.prerelease.length && !s.prerelease.length)
        return -1;
      if (!this.prerelease.length && s.prerelease.length)
        return 1;
      if (!this.prerelease.length && !s.prerelease.length)
        return 0;
      let l = 0;
      do {
        const u = this.prerelease[l], h = s.prerelease[l];
        if (e("prerelease compare", l, u, h), u === void 0 && h === void 0)
          return 0;
        if (h === void 0)
          return 1;
        if (u === void 0)
          return -1;
        if (u === h)
          continue;
        return o(u, h);
      } while (++l);
    }
    compareBuild(s) {
      s instanceof d || (s = new d(s, this.options));
      let l = 0;
      do {
        const u = this.build[l], h = s.build[l];
        if (e("build compare", l, u, h), u === void 0 && h === void 0)
          return 0;
        if (h === void 0)
          return 1;
        if (u === void 0)
          return -1;
        if (u === h)
          continue;
        return o(u, h);
      } while (++l);
    }
    // preminor will bump the version up to the next minor release, and immediately
    // down to pre-release. premajor and prepatch work the same way.
    inc(s, l, u) {
      if (s.startsWith("pre")) {
        if (!l && u === !1)
          throw new Error("invalid increment argument: identifier is empty");
        if (l) {
          const h = `-${l}`.match(this.options.loose ? n[i.PRERELEASELOOSE] : n[i.PRERELEASE]);
          if (!h || h[1] !== l)
            throw new Error(`invalid identifier: ${l}`);
        }
      }
      switch (s) {
        case "premajor":
          this.prerelease.length = 0, this.patch = 0, this.minor = 0, this.major++, this.inc("pre", l, u);
          break;
        case "preminor":
          this.prerelease.length = 0, this.patch = 0, this.minor++, this.inc("pre", l, u);
          break;
        case "prepatch":
          this.prerelease.length = 0, this.inc("patch", l, u), this.inc("pre", l, u);
          break;
        // If the input is a non-prerelease version, this acts the same as
        // prepatch.
        case "prerelease":
          this.prerelease.length === 0 && this.inc("patch", l, u), this.inc("pre", l, u);
          break;
        case "release":
          if (this.prerelease.length === 0)
            throw new Error(`version ${this.raw} is not a prerelease`);
          this.prerelease.length = 0;
          break;
        case "major":
          (this.minor !== 0 || this.patch !== 0 || this.prerelease.length === 0) && this.major++, this.minor = 0, this.patch = 0, this.prerelease = [];
          break;
        case "minor":
          (this.patch !== 0 || this.prerelease.length === 0) && this.minor++, this.patch = 0, this.prerelease = [];
          break;
        case "patch":
          this.prerelease.length === 0 && this.patch++, this.prerelease = [];
          break;
        // This probably shouldn't be used publicly.
        // 1.0.0 'pre' would become 1.0.0-0 which is the wrong direction.
        case "pre": {
          const h = Number(u) ? 1 : 0;
          if (this.prerelease.length === 0)
            this.prerelease = [h];
          else {
            let p = this.prerelease.length;
            for (; --p >= 0; )
              typeof this.prerelease[p] == "number" && (this.prerelease[p]++, p = -2);
            if (p === -1) {
              if (l === this.prerelease.join(".") && u === !1)
                throw new Error("invalid increment argument: identifier already exists");
              this.prerelease.push(h);
            }
          }
          if (l) {
            let p = [l, h];
            u === !1 && (p = [l]), o(this.prerelease[0], l) === 0 ? isNaN(this.prerelease[1]) && (this.prerelease = p) : this.prerelease = p;
          }
          break;
        }
        default:
          throw new Error(`invalid increment argument: ${s}`);
      }
      return this.raw = this.format(), this.build.length && (this.raw += `+${this.build.join(".")}`), this;
    }
  }
  return lr = d, lr;
}
var cr, kn;
function We() {
  if (kn) return cr;
  kn = 1;
  const e = ie();
  return cr = (r, n, i = !1) => {
    if (r instanceof e)
      return r;
    try {
      return new e(r, n);
    } catch (a) {
      if (!i)
        return null;
      throw a;
    }
  }, cr;
}
var dr, jn;
function Mo() {
  if (jn) return dr;
  jn = 1;
  const e = We();
  return dr = (r, n) => {
    const i = e(r, n);
    return i ? i.version : null;
  }, dr;
}
var ur, Zn;
function Ho() {
  if (Zn) return ur;
  Zn = 1;
  const e = We();
  return ur = (r, n) => {
    const i = e(r.trim().replace(/^[=v]+/, ""), n);
    return i ? i.version : null;
  }, ur;
}
var hr, Un;
function Vo() {
  if (Un) return hr;
  Un = 1;
  const e = ie();
  return hr = (r, n, i, a, o) => {
    typeof i == "string" && (o = a, a = i, i = void 0);
    try {
      return new e(
        r instanceof e ? r.version : r,
        i
      ).inc(n, a, o).version;
    } catch {
      return null;
    }
  }, hr;
}
var fr, Bn;
function qo() {
  if (Bn) return fr;
  Bn = 1;
  const e = We();
  return fr = (r, n) => {
    const i = e(r, null, !0), a = e(n, null, !0), o = i.compare(a);
    if (o === 0)
      return null;
    const d = o > 0, c = d ? i : a, s = d ? a : i, l = !!c.prerelease.length;
    if (!!s.prerelease.length && !l) {
      if (!s.patch && !s.minor)
        return "major";
      if (s.compareMain(c) === 0)
        return s.minor && !s.patch ? "minor" : "patch";
    }
    const h = l ? "pre" : "";
    return i.major !== a.major ? h + "major" : i.minor !== a.minor ? h + "minor" : i.patch !== a.patch ? h + "patch" : "prerelease";
  }, fr;
}
var pr, Gn;
function Fo() {
  if (Gn) return pr;
  Gn = 1;
  const e = ie();
  return pr = (r, n) => new e(r, n).major, pr;
}
var mr, Xn;
function ko() {
  if (Xn) return mr;
  Xn = 1;
  const e = ie();
  return mr = (r, n) => new e(r, n).minor, mr;
}
var gr, Wn;
function jo() {
  if (Wn) return gr;
  Wn = 1;
  const e = ie();
  return gr = (r, n) => new e(r, n).patch, gr;
}
var vr, Yn;
function Zo() {
  if (Yn) return vr;
  Yn = 1;
  const e = We();
  return vr = (r, n) => {
    const i = e(r, n);
    return i && i.prerelease.length ? i.prerelease : null;
  }, vr;
}
var Er, zn;
function Ee() {
  if (zn) return Er;
  zn = 1;
  const e = ie();
  return Er = (r, n, i) => new e(r, i).compare(new e(n, i)), Er;
}
var br, Jn;
function Uo() {
  if (Jn) return br;
  Jn = 1;
  const e = Ee();
  return br = (r, n, i) => e(n, r, i), br;
}
var wr, Kn;
function Bo() {
  if (Kn) return wr;
  Kn = 1;
  const e = Ee();
  return wr = (r, n) => e(r, n, !0), wr;
}
var yr, Qn;
function pn() {
  if (Qn) return yr;
  Qn = 1;
  const e = ie();
  return yr = (r, n, i) => {
    const a = new e(r, i), o = new e(n, i);
    return a.compare(o) || a.compareBuild(o);
  }, yr;
}
var Rr, ei;
function Go() {
  if (ei) return Rr;
  ei = 1;
  const e = pn();
  return Rr = (r, n) => r.sort((i, a) => e(i, a, n)), Rr;
}
var $r, ti;
function Xo() {
  if (ti) return $r;
  ti = 1;
  const e = pn();
  return $r = (r, n) => r.sort((i, a) => e(a, i, n)), $r;
}
var Ir, ri;
function jt() {
  if (ri) return Ir;
  ri = 1;
  const e = Ee();
  return Ir = (r, n, i) => e(r, n, i) > 0, Ir;
}
var Sr, ni;
function mn() {
  if (ni) return Sr;
  ni = 1;
  const e = Ee();
  return Sr = (r, n, i) => e(r, n, i) < 0, Sr;
}
var Tr, ii;
function ra() {
  if (ii) return Tr;
  ii = 1;
  const e = Ee();
  return Tr = (r, n, i) => e(r, n, i) === 0, Tr;
}
var Ar, ai;
function na() {
  if (ai) return Ar;
  ai = 1;
  const e = Ee();
  return Ar = (r, n, i) => e(r, n, i) !== 0, Ar;
}
var Cr, oi;
function gn() {
  if (oi) return Cr;
  oi = 1;
  const e = Ee();
  return Cr = (r, n, i) => e(r, n, i) >= 0, Cr;
}
var xr, si;
function vn() {
  if (si) return xr;
  si = 1;
  const e = Ee();
  return xr = (r, n, i) => e(r, n, i) <= 0, xr;
}
var Dr, li;
function ia() {
  if (li) return Dr;
  li = 1;
  const e = ra(), t = na(), r = jt(), n = gn(), i = mn(), a = vn();
  return Dr = (d, c, s, l) => {
    switch (c) {
      case "===":
        return typeof d == "object" && (d = d.version), typeof s == "object" && (s = s.version), d === s;
      case "!==":
        return typeof d == "object" && (d = d.version), typeof s == "object" && (s = s.version), d !== s;
      case "":
      case "=":
      case "==":
        return e(d, s, l);
      case "!=":
        return t(d, s, l);
      case ">":
        return r(d, s, l);
      case ">=":
        return n(d, s, l);
      case "<":
        return i(d, s, l);
      case "<=":
        return a(d, s, l);
      default:
        throw new TypeError(`Invalid operator: ${c}`);
    }
  }, Dr;
}
var Or, ci;
function Wo() {
  if (ci) return Or;
  ci = 1;
  const e = ie(), t = We(), { safeRe: r, t: n } = dt();
  return Or = (a, o) => {
    if (a instanceof e)
      return a;
    if (typeof a == "number" && (a = String(a)), typeof a != "string")
      return null;
    o = o || {};
    let d = null;
    if (!o.rtl)
      d = a.match(o.includePrerelease ? r[n.COERCEFULL] : r[n.COERCE]);
    else {
      const p = o.includePrerelease ? r[n.COERCERTLFULL] : r[n.COERCERTL];
      let E;
      for (; (E = p.exec(a)) && (!d || d.index + d[0].length !== a.length); )
        (!d || E.index + E[0].length !== d.index + d[0].length) && (d = E), p.lastIndex = E.index + E[1].length + E[2].length;
      p.lastIndex = -1;
    }
    if (d === null)
      return null;
    const c = d[2], s = d[3] || "0", l = d[4] || "0", u = o.includePrerelease && d[5] ? `-${d[5]}` : "", h = o.includePrerelease && d[6] ? `+${d[6]}` : "";
    return t(`${c}.${s}.${l}${u}${h}`, o);
  }, Or;
}
var _r, di;
function Yo() {
  if (di) return _r;
  di = 1;
  class e {
    constructor() {
      this.max = 1e3, this.map = /* @__PURE__ */ new Map();
    }
    get(r) {
      const n = this.map.get(r);
      if (n !== void 0)
        return this.map.delete(r), this.map.set(r, n), n;
    }
    delete(r) {
      return this.map.delete(r);
    }
    set(r, n) {
      if (!this.delete(r) && n !== void 0) {
        if (this.map.size >= this.max) {
          const a = this.map.keys().next().value;
          this.delete(a);
        }
        this.map.set(r, n);
      }
      return this;
    }
  }
  return _r = e, _r;
}
var Pr, ui;
function be() {
  if (ui) return Pr;
  ui = 1;
  const e = /\s+/g;
  class t {
    constructor(m, w) {
      if (w = i(w), m instanceof t)
        return m.loose === !!w.loose && m.includePrerelease === !!w.includePrerelease ? m : new t(m.raw, w);
      if (m instanceof a)
        return this.raw = m.value, this.set = [[m]], this.formatted = void 0, this;
      if (this.options = w, this.loose = !!w.loose, this.includePrerelease = !!w.includePrerelease, this.raw = m.trim().replace(e, " "), this.set = this.raw.split("||").map((b) => this.parseRange(b.trim())).filter((b) => b.length), !this.set.length)
        throw new TypeError(`Invalid SemVer Range: ${this.raw}`);
      if (this.set.length > 1) {
        const b = this.set[0];
        if (this.set = this.set.filter((R) => !f(R[0])), this.set.length === 0)
          this.set = [b];
        else if (this.set.length > 1) {
          for (const R of this.set)
            if (R.length === 1 && $(R[0])) {
              this.set = [R];
              break;
            }
        }
      }
      this.formatted = void 0;
    }
    get range() {
      if (this.formatted === void 0) {
        this.formatted = "";
        for (let m = 0; m < this.set.length; m++) {
          m > 0 && (this.formatted += "||");
          const w = this.set[m];
          for (let b = 0; b < w.length; b++)
            b > 0 && (this.formatted += " "), this.formatted += w[b].toString().trim();
        }
      }
      return this.formatted;
    }
    format() {
      return this.range;
    }
    toString() {
      return this.range;
    }
    parseRange(m) {
      const b = ((this.options.includePrerelease && p) | (this.options.loose && E)) + ":" + m, R = n.get(b);
      if (R)
        return R;
      const y = this.options.loose, C = y ? c[s.HYPHENRANGELOOSE] : c[s.HYPHENRANGE];
      m = m.replace(C, Ie(this.options.includePrerelease)), o("hyphen replace", m), m = m.replace(c[s.COMPARATORTRIM], l), o("comparator trim", m), m = m.replace(c[s.TILDETRIM], u), o("tilde trim", m), m = m.replace(c[s.CARETTRIM], h), o("caret trim", m);
      let V = m.split(" ").map((W) => H(W, this.options)).join(" ").split(/\s+/).map((W) => ue(W, this.options));
      y && (V = V.filter((W) => (o("loose invalid filter", W, this.options), !!W.match(c[s.COMPARATORLOOSE])))), o("range list", V);
      const O = /* @__PURE__ */ new Map(), j = V.map((W) => new a(W, this.options));
      for (const W of j) {
        if (f(W))
          return [W];
        O.set(W.value, W);
      }
      O.size > 1 && O.has("") && O.delete("");
      const te = [...O.values()];
      return n.set(b, te), te;
    }
    intersects(m, w) {
      if (!(m instanceof t))
        throw new TypeError("a Range is required");
      return this.set.some((b) => _(b, w) && m.set.some((R) => _(R, w) && b.every((y) => R.every((C) => y.intersects(C, w)))));
    }
    // if ANY of the sets match ALL of its comparators, then pass
    test(m) {
      if (!m)
        return !1;
      if (typeof m == "string")
        try {
          m = new d(m, this.options);
        } catch {
          return !1;
        }
      for (let w = 0; w < this.set.length; w++)
        if (le(this.set[w], m, this.options))
          return !0;
      return !1;
    }
  }
  Pr = t;
  const r = Yo(), n = new r(), i = fn(), a = Zt(), o = kt(), d = ie(), {
    safeRe: c,
    t: s,
    comparatorTrimReplace: l,
    tildeTrimReplace: u,
    caretTrimReplace: h
  } = dt(), { FLAG_INCLUDE_PRERELEASE: p, FLAG_LOOSE: E } = Ft(), f = (v) => v.value === "<0.0.0-0", $ = (v) => v.value === "", _ = (v, m) => {
    let w = !0;
    const b = v.slice();
    let R = b.pop();
    for (; w && b.length; )
      w = b.every((y) => R.intersects(y, m)), R = b.pop();
    return w;
  }, H = (v, m) => (v = v.replace(c[s.BUILD], ""), o("comp", v, m), v = L(v, m), o("caret", v), v = q(v, m), o("tildes", v), v = T(v, m), o("xrange", v), v = $e(v, m), o("stars", v), v), D = (v) => !v || v.toLowerCase() === "x" || v === "*", q = (v, m) => v.trim().split(/\s+/).map((w) => B(w, m)).join(" "), B = (v, m) => {
    const w = m.loose ? c[s.TILDELOOSE] : c[s.TILDE];
    return v.replace(w, (b, R, y, C, V) => {
      o("tilde", v, b, R, y, C, V);
      let O;
      return D(R) ? O = "" : D(y) ? O = `>=${R}.0.0 <${+R + 1}.0.0-0` : D(C) ? O = `>=${R}.${y}.0 <${R}.${+y + 1}.0-0` : V ? (o("replaceTilde pr", V), O = `>=${R}.${y}.${C}-${V} <${R}.${+y + 1}.0-0`) : O = `>=${R}.${y}.${C} <${R}.${+y + 1}.0-0`, o("tilde return", O), O;
    });
  }, L = (v, m) => v.trim().split(/\s+/).map((w) => k(w, m)).join(" "), k = (v, m) => {
    o("caret", v, m);
    const w = m.loose ? c[s.CARETLOOSE] : c[s.CARET], b = m.includePrerelease ? "-0" : "";
    return v.replace(w, (R, y, C, V, O) => {
      o("caret", v, R, y, C, V, O);
      let j;
      return D(y) ? j = "" : D(C) ? j = `>=${y}.0.0${b} <${+y + 1}.0.0-0` : D(V) ? y === "0" ? j = `>=${y}.${C}.0${b} <${y}.${+C + 1}.0-0` : j = `>=${y}.${C}.0${b} <${+y + 1}.0.0-0` : O ? (o("replaceCaret pr", O), y === "0" ? C === "0" ? j = `>=${y}.${C}.${V}-${O} <${y}.${C}.${+V + 1}-0` : j = `>=${y}.${C}.${V}-${O} <${y}.${+C + 1}.0-0` : j = `>=${y}.${C}.${V}-${O} <${+y + 1}.0.0-0`) : (o("no pr"), y === "0" ? C === "0" ? j = `>=${y}.${C}.${V}${b} <${y}.${C}.${+V + 1}-0` : j = `>=${y}.${C}.${V}${b} <${y}.${+C + 1}.0-0` : j = `>=${y}.${C}.${V} <${+y + 1}.0.0-0`), o("caret return", j), j;
    });
  }, T = (v, m) => (o("replaceXRanges", v, m), v.split(/\s+/).map((w) => J(w, m)).join(" ")), J = (v, m) => {
    v = v.trim();
    const w = m.loose ? c[s.XRANGELOOSE] : c[s.XRANGE];
    return v.replace(w, (b, R, y, C, V, O) => {
      o("xRange", v, b, R, y, C, V, O);
      const j = D(y), te = j || D(C), W = te || D(V), Ye = W;
      return R === "=" && Ye && (R = ""), O = m.includePrerelease ? "-0" : "", j ? R === ">" || R === "<" ? b = "<0.0.0-0" : b = "*" : R && Ye ? (te && (C = 0), V = 0, R === ">" ? (R = ">=", te ? (y = +y + 1, C = 0, V = 0) : (C = +C + 1, V = 0)) : R === "<=" && (R = "<", te ? y = +y + 1 : C = +C + 1), R === "<" && (O = "-0"), b = `${R + y}.${C}.${V}${O}`) : te ? b = `>=${y}.0.0${O} <${+y + 1}.0.0-0` : W && (b = `>=${y}.${C}.0${O} <${y}.${+C + 1}.0-0`), o("xRange return", b), b;
    });
  }, $e = (v, m) => (o("replaceStars", v, m), v.trim().replace(c[s.STAR], "")), ue = (v, m) => (o("replaceGTE0", v, m), v.trim().replace(c[m.includePrerelease ? s.GTE0PRE : s.GTE0], "")), Ie = (v) => (m, w, b, R, y, C, V, O, j, te, W, Ye) => (D(b) ? w = "" : D(R) ? w = `>=${b}.0.0${v ? "-0" : ""}` : D(y) ? w = `>=${b}.${R}.0${v ? "-0" : ""}` : C ? w = `>=${w}` : w = `>=${w}${v ? "-0" : ""}`, D(j) ? O = "" : D(te) ? O = `<${+j + 1}.0.0-0` : D(W) ? O = `<${j}.${+te + 1}.0-0` : Ye ? O = `<=${j}.${te}.${W}-${Ye}` : v ? O = `<${j}.${te}.${+W + 1}-0` : O = `<=${O}`, `${w} ${O}`.trim()), le = (v, m, w) => {
    for (let b = 0; b < v.length; b++)
      if (!v[b].test(m))
        return !1;
    if (m.prerelease.length && !w.includePrerelease) {
      for (let b = 0; b < v.length; b++)
        if (o(v[b].semver), v[b].semver !== a.ANY && v[b].semver.prerelease.length > 0) {
          const R = v[b].semver;
          if (R.major === m.major && R.minor === m.minor && R.patch === m.patch)
            return !0;
        }
      return !1;
    }
    return !0;
  };
  return Pr;
}
var Nr, hi;
function Zt() {
  if (hi) return Nr;
  hi = 1;
  const e = Symbol("SemVer ANY");
  class t {
    static get ANY() {
      return e;
    }
    constructor(l, u) {
      if (u = r(u), l instanceof t) {
        if (l.loose === !!u.loose)
          return l;
        l = l.value;
      }
      l = l.trim().split(/\s+/).join(" "), o("comparator", l, u), this.options = u, this.loose = !!u.loose, this.parse(l), this.semver === e ? this.value = "" : this.value = this.operator + this.semver.version, o("comp", this);
    }
    parse(l) {
      const u = this.options.loose ? n[i.COMPARATORLOOSE] : n[i.COMPARATOR], h = l.match(u);
      if (!h)
        throw new TypeError(`Invalid comparator: ${l}`);
      this.operator = h[1] !== void 0 ? h[1] : "", this.operator === "=" && (this.operator = ""), h[2] ? this.semver = new d(h[2], this.options.loose) : this.semver = e;
    }
    toString() {
      return this.value;
    }
    test(l) {
      if (o("Comparator.test", l, this.options.loose), this.semver === e || l === e)
        return !0;
      if (typeof l == "string")
        try {
          l = new d(l, this.options);
        } catch {
          return !1;
        }
      return a(l, this.operator, this.semver, this.options);
    }
    intersects(l, u) {
      if (!(l instanceof t))
        throw new TypeError("a Comparator is required");
      return this.operator === "" ? this.value === "" ? !0 : new c(l.value, u).test(this.value) : l.operator === "" ? l.value === "" ? !0 : new c(this.value, u).test(l.semver) : (u = r(u), u.includePrerelease && (this.value === "<0.0.0-0" || l.value === "<0.0.0-0") || !u.includePrerelease && (this.value.startsWith("<0.0.0") || l.value.startsWith("<0.0.0")) ? !1 : !!(this.operator.startsWith(">") && l.operator.startsWith(">") || this.operator.startsWith("<") && l.operator.startsWith("<") || this.semver.version === l.semver.version && this.operator.includes("=") && l.operator.includes("=") || a(this.semver, "<", l.semver, u) && this.operator.startsWith(">") && l.operator.startsWith("<") || a(this.semver, ">", l.semver, u) && this.operator.startsWith("<") && l.operator.startsWith(">")));
    }
  }
  Nr = t;
  const r = fn(), { safeRe: n, t: i } = dt(), a = ia(), o = kt(), d = ie(), c = be();
  return Nr;
}
var Lr, fi;
function Ut() {
  if (fi) return Lr;
  fi = 1;
  const e = be();
  return Lr = (r, n, i) => {
    try {
      n = new e(n, i);
    } catch {
      return !1;
    }
    return n.test(r);
  }, Lr;
}
var Mr, pi;
function zo() {
  if (pi) return Mr;
  pi = 1;
  const e = be();
  return Mr = (r, n) => new e(r, n).set.map((i) => i.map((a) => a.value).join(" ").trim().split(" ")), Mr;
}
var Hr, mi;
function Jo() {
  if (mi) return Hr;
  mi = 1;
  const e = ie(), t = be();
  return Hr = (n, i, a) => {
    let o = null, d = null, c = null;
    try {
      c = new t(i, a);
    } catch {
      return null;
    }
    return n.forEach((s) => {
      c.test(s) && (!o || d.compare(s) === -1) && (o = s, d = new e(o, a));
    }), o;
  }, Hr;
}
var Vr, gi;
function Ko() {
  if (gi) return Vr;
  gi = 1;
  const e = ie(), t = be();
  return Vr = (n, i, a) => {
    let o = null, d = null, c = null;
    try {
      c = new t(i, a);
    } catch {
      return null;
    }
    return n.forEach((s) => {
      c.test(s) && (!o || d.compare(s) === 1) && (o = s, d = new e(o, a));
    }), o;
  }, Vr;
}
var qr, vi;
function Qo() {
  if (vi) return qr;
  vi = 1;
  const e = ie(), t = be(), r = jt();
  return qr = (i, a) => {
    i = new t(i, a);
    let o = new e("0.0.0");
    if (i.test(o) || (o = new e("0.0.0-0"), i.test(o)))
      return o;
    o = null;
    for (let d = 0; d < i.set.length; ++d) {
      const c = i.set[d];
      let s = null;
      c.forEach((l) => {
        const u = new e(l.semver.version);
        switch (l.operator) {
          case ">":
            u.prerelease.length === 0 ? u.patch++ : u.prerelease.push(0), u.raw = u.format();
          /* fallthrough */
          case "":
          case ">=":
            (!s || r(u, s)) && (s = u);
            break;
          case "<":
          case "<=":
            break;
          /* istanbul ignore next */
          default:
            throw new Error(`Unexpected operation: ${l.operator}`);
        }
      }), s && (!o || r(o, s)) && (o = s);
    }
    return o && i.test(o) ? o : null;
  }, qr;
}
var Fr, Ei;
function es() {
  if (Ei) return Fr;
  Ei = 1;
  const e = be();
  return Fr = (r, n) => {
    try {
      return new e(r, n).range || "*";
    } catch {
      return null;
    }
  }, Fr;
}
var kr, bi;
function En() {
  if (bi) return kr;
  bi = 1;
  const e = ie(), t = Zt(), { ANY: r } = t, n = be(), i = Ut(), a = jt(), o = mn(), d = vn(), c = gn();
  return kr = (l, u, h, p) => {
    l = new e(l, p), u = new n(u, p);
    let E, f, $, _, H;
    switch (h) {
      case ">":
        E = a, f = d, $ = o, _ = ">", H = ">=";
        break;
      case "<":
        E = o, f = c, $ = a, _ = "<", H = "<=";
        break;
      default:
        throw new TypeError('Must provide a hilo val of "<" or ">"');
    }
    if (i(l, u, p))
      return !1;
    for (let D = 0; D < u.set.length; ++D) {
      const q = u.set[D];
      let B = null, L = null;
      if (q.forEach((k) => {
        k.semver === r && (k = new t(">=0.0.0")), B = B || k, L = L || k, E(k.semver, B.semver, p) ? B = k : $(k.semver, L.semver, p) && (L = k);
      }), B.operator === _ || B.operator === H || (!L.operator || L.operator === _) && f(l, L.semver))
        return !1;
      if (L.operator === H && $(l, L.semver))
        return !1;
    }
    return !0;
  }, kr;
}
var jr, wi;
function ts() {
  if (wi) return jr;
  wi = 1;
  const e = En();
  return jr = (r, n, i) => e(r, n, ">", i), jr;
}
var Zr, yi;
function rs() {
  if (yi) return Zr;
  yi = 1;
  const e = En();
  return Zr = (r, n, i) => e(r, n, "<", i), Zr;
}
var Ur, Ri;
function ns() {
  if (Ri) return Ur;
  Ri = 1;
  const e = be();
  return Ur = (r, n, i) => (r = new e(r, i), n = new e(n, i), r.intersects(n, i)), Ur;
}
var Br, $i;
function is() {
  if ($i) return Br;
  $i = 1;
  const e = Ut(), t = Ee();
  return Br = (r, n, i) => {
    const a = [];
    let o = null, d = null;
    const c = r.sort((h, p) => t(h, p, i));
    for (const h of c)
      e(h, n, i) ? (d = h, o || (o = h)) : (d && a.push([o, d]), d = null, o = null);
    o && a.push([o, null]);
    const s = [];
    for (const [h, p] of a)
      h === p ? s.push(h) : !p && h === c[0] ? s.push("*") : p ? h === c[0] ? s.push(`<=${p}`) : s.push(`${h} - ${p}`) : s.push(`>=${h}`);
    const l = s.join(" || "), u = typeof n.raw == "string" ? n.raw : String(n);
    return l.length < u.length ? l : n;
  }, Br;
}
var Gr, Ii;
function as() {
  if (Ii) return Gr;
  Ii = 1;
  const e = be(), t = Zt(), { ANY: r } = t, n = Ut(), i = Ee(), a = (u, h, p = {}) => {
    if (u === h)
      return !0;
    u = new e(u, p), h = new e(h, p);
    let E = !1;
    e: for (const f of u.set) {
      for (const $ of h.set) {
        const _ = c(f, $, p);
        if (E = E || _ !== null, _)
          continue e;
      }
      if (E)
        return !1;
    }
    return !0;
  }, o = [new t(">=0.0.0-0")], d = [new t(">=0.0.0")], c = (u, h, p) => {
    if (u === h)
      return !0;
    if (u.length === 1 && u[0].semver === r) {
      if (h.length === 1 && h[0].semver === r)
        return !0;
      p.includePrerelease ? u = o : u = d;
    }
    if (h.length === 1 && h[0].semver === r) {
      if (p.includePrerelease)
        return !0;
      h = d;
    }
    const E = /* @__PURE__ */ new Set();
    let f, $;
    for (const T of u)
      T.operator === ">" || T.operator === ">=" ? f = s(f, T, p) : T.operator === "<" || T.operator === "<=" ? $ = l($, T, p) : E.add(T.semver);
    if (E.size > 1)
      return null;
    let _;
    if (f && $) {
      if (_ = i(f.semver, $.semver, p), _ > 0)
        return null;
      if (_ === 0 && (f.operator !== ">=" || $.operator !== "<="))
        return null;
    }
    for (const T of E) {
      if (f && !n(T, String(f), p) || $ && !n(T, String($), p))
        return null;
      for (const J of h)
        if (!n(T, String(J), p))
          return !1;
      return !0;
    }
    let H, D, q, B, L = $ && !p.includePrerelease && $.semver.prerelease.length ? $.semver : !1, k = f && !p.includePrerelease && f.semver.prerelease.length ? f.semver : !1;
    L && L.prerelease.length === 1 && $.operator === "<" && L.prerelease[0] === 0 && (L = !1);
    for (const T of h) {
      if (B = B || T.operator === ">" || T.operator === ">=", q = q || T.operator === "<" || T.operator === "<=", f) {
        if (k && T.semver.prerelease && T.semver.prerelease.length && T.semver.major === k.major && T.semver.minor === k.minor && T.semver.patch === k.patch && (k = !1), T.operator === ">" || T.operator === ">=") {
          if (H = s(f, T, p), H === T && H !== f)
            return !1;
        } else if (f.operator === ">=" && !n(f.semver, String(T), p))
          return !1;
      }
      if ($) {
        if (L && T.semver.prerelease && T.semver.prerelease.length && T.semver.major === L.major && T.semver.minor === L.minor && T.semver.patch === L.patch && (L = !1), T.operator === "<" || T.operator === "<=") {
          if (D = l($, T, p), D === T && D !== $)
            return !1;
        } else if ($.operator === "<=" && !n($.semver, String(T), p))
          return !1;
      }
      if (!T.operator && ($ || f) && _ !== 0)
        return !1;
    }
    return !(f && q && !$ && _ !== 0 || $ && B && !f && _ !== 0 || k || L);
  }, s = (u, h, p) => {
    if (!u)
      return h;
    const E = i(u.semver, h.semver, p);
    return E > 0 ? u : E < 0 || h.operator === ">" && u.operator === ">=" ? h : u;
  }, l = (u, h, p) => {
    if (!u)
      return h;
    const E = i(u.semver, h.semver, p);
    return E < 0 ? u : E > 0 || h.operator === "<" && u.operator === "<=" ? h : u;
  };
  return Gr = a, Gr;
}
var Xr, Si;
function os() {
  if (Si) return Xr;
  Si = 1;
  const e = dt(), t = Ft(), r = ie(), n = ta(), i = We(), a = Mo(), o = Ho(), d = Vo(), c = qo(), s = Fo(), l = ko(), u = jo(), h = Zo(), p = Ee(), E = Uo(), f = Bo(), $ = pn(), _ = Go(), H = Xo(), D = jt(), q = mn(), B = ra(), L = na(), k = gn(), T = vn(), J = ia(), $e = Wo(), ue = Zt(), Ie = be(), le = Ut(), v = zo(), m = Jo(), w = Ko(), b = Qo(), R = es(), y = En(), C = ts(), V = rs(), O = ns(), j = is(), te = as();
  return Xr = {
    parse: i,
    valid: a,
    clean: o,
    inc: d,
    diff: c,
    major: s,
    minor: l,
    patch: u,
    prerelease: h,
    compare: p,
    rcompare: E,
    compareLoose: f,
    compareBuild: $,
    sort: _,
    rsort: H,
    gt: D,
    lt: q,
    eq: B,
    neq: L,
    gte: k,
    lte: T,
    cmp: J,
    coerce: $e,
    Comparator: ue,
    Range: Ie,
    satisfies: le,
    toComparators: v,
    maxSatisfying: m,
    minSatisfying: w,
    minVersion: b,
    validRange: R,
    outside: y,
    gtr: C,
    ltr: V,
    intersects: O,
    simplifyRange: j,
    subset: te,
    SemVer: r,
    re: e.re,
    src: e.src,
    tokens: e.t,
    SEMVER_SPEC_VERSION: t.SEMVER_SPEC_VERSION,
    RELEASE_TYPES: t.RELEASE_TYPES,
    compareIdentifiers: n.compareIdentifiers,
    rcompareIdentifiers: n.rcompareIdentifiers
  }, Xr;
}
var ss = os();
const st = /* @__PURE__ */ Sa(ss), Ti = (e) => st.valid(e) ?? st.valid(st.coerce(e)), ls = window.Vaadin.copilot.eventbus;
ls.on("serverInfo", (e) => {
  const r = e.detail.versions.find((a) => a.name === "Vaadin");
  if (!r)
    return;
  const n = ze.getMostRecentVaadinVersion(), i = r.version;
  xi(`${Ta}get-release-note-url`, { version: i }, (a) => {
    const o = a.data;
    if (!o.error && o.url)
      if (n) {
        const d = Ti(i), c = Ti(n), s = d !== null && c !== null ? st.gte(d, c) : i === n;
        F.setProjectVersionReleaseNoteInfo({
          mostRecentVersion: s,
          vaadinVersion: i,
          url: o.url
        }), d !== null && c !== null && st.gt(d, c) && (_e({
          type: oe.INFORMATION,
          message: `You're now on version ${i}!`,
          details: Lt(
            x`<a class="ahreflike" href="${o.url}" target="_blank">Click here to open release notes!</a>`
          )
        }), ze.setMostRecentReleaseNoteDismissed(!1), ze.setMostRecentVaadinVersion(i));
      } else
        ze.setMostRecentReleaseNoteDismissed(!1), ze.setMostRecentVaadinVersion(i), F.setProjectVersionReleaseNoteInfo({
          mostRecentVersion: !0,
          vaadinVersion: i,
          url: o.url
        });
  });
});
X.on("copilot-java-after-update", (e) => {
  e.detail.classes.find((t) => t.routePath !== void 0) && X.emit("update-routes", {});
});
