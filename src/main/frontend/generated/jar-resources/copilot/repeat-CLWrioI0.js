import { a6 as E, ae as S, p as T, l as R, n as k, af as U } from "./copilot-Do4z_cRr.js";
var A, w;
function I() {
  return w || (w = 1, A = function() {
    var e = document.getSelection();
    if (!e.rangeCount)
      return function() {
      };
    for (var o = document.activeElement, n = [], f = 0; f < e.rangeCount; f++)
      n.push(e.getRangeAt(f));
    switch (o.tagName.toUpperCase()) {
      // .toUpperCase handles XHTML
      case "INPUT":
      case "TEXTAREA":
        o.blur();
        break;
      default:
        o = null;
        break;
    }
    return e.removeAllRanges(), function() {
      e.type === "Caret" && e.removeAllRanges(), e.rangeCount || n.forEach(function(r) {
        e.addRange(r);
      }), o && o.focus();
    };
  }), A;
}
var C, x;
function B() {
  if (x) return C;
  x = 1;
  var e = I(), o = {
    "text/plain": "Text",
    "text/html": "Url",
    default: "Text"
  }, n = "Copy to clipboard: #{key}, Enter";
  function f(s) {
    var t = (/mac os x/i.test(navigator.userAgent) ? "⌘" : "Ctrl") + "+C";
    return s.replace(/#{\s*key\s*}/g, t);
  }
  function r(s, t) {
    var c, u, g, m, l, a, i = !1;
    t || (t = {}), c = t.debug || !1;
    try {
      g = e(), m = document.createRange(), l = document.getSelection(), a = document.createElement("span"), a.textContent = s, a.ariaHidden = "true", a.style.all = "unset", a.style.position = "fixed", a.style.top = 0, a.style.clip = "rect(0, 0, 0, 0)", a.style.whiteSpace = "pre", a.style.webkitUserSelect = "text", a.style.MozUserSelect = "text", a.style.msUserSelect = "text", a.style.userSelect = "text", a.addEventListener("copy", function(d) {
        if (d.stopPropagation(), t.format)
          if (d.preventDefault(), typeof d.clipboardData > "u") {
            c && console.warn("unable to use e.clipboardData"), c && console.warn("trying IE specific stuff"), window.clipboardData.clearData();
            var y = o[t.format] || o.default;
            window.clipboardData.setData(y, s);
          } else
            d.clipboardData.clearData(), d.clipboardData.setData(t.format, s);
        t.onCopy && (d.preventDefault(), t.onCopy(d.clipboardData));
      }), document.body.appendChild(a), m.selectNodeContents(a), l.addRange(m);
      var p = document.execCommand("copy");
      if (!p)
        throw new Error("copy command was unsuccessful");
      i = !0;
    } catch (d) {
      c && console.error("unable to copy using execCommand: ", d), c && console.warn("trying IE specific stuff");
      try {
        window.clipboardData.setData(t.format || "text", s), t.onCopy && t.onCopy(window.clipboardData), i = !0;
      } catch (y) {
        c && console.error("unable to copy using clipboardData: ", y), c && console.error("falling back to prompt"), u = f("message" in t ? t.message : n), window.prompt(u, s);
      }
    } finally {
      l && (typeof l.removeRange == "function" ? l.removeRange(m) : l.removeAllRanges()), a && document.body.removeChild(a), g();
    }
    return i;
  }
  return C = r, C;
}
var M = B();
const F = /* @__PURE__ */ E(M);
/**
 * @license
 * Copyright 2020 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */
const { I: P } = S, L = (e) => e.strings === void 0, D = () => document.createComment(""), b = (e, o, n) => {
  const f = e._$AA.parentNode, r = o === void 0 ? e._$AB : o._$AA;
  if (n === void 0) {
    const s = f.insertBefore(D(), r), t = f.insertBefore(D(), r);
    n = new P(s, t, e, e.options);
  } else {
    const s = n._$AB.nextSibling, t = n._$AM, c = t !== e;
    if (c) {
      let u;
      n._$AQ?.(e), n._$AM = e, n._$AP !== void 0 && (u = e._$AU) !== t._$AU && n._$AP(u);
    }
    if (s !== r || c) {
      let u = n._$AA;
      for (; u !== s; ) {
        const g = u.nextSibling;
        f.insertBefore(u, r), u = g;
      }
    }
  }
  return n;
}, v = (e, o, n = e) => (e._$AI(o, n), e), q = {}, H = (e, o = q) => e._$AH = o, N = (e) => e._$AH, $ = (e) => {
  e._$AP?.(!1, !0);
  let o = e._$AA;
  const n = e._$AB.nextSibling;
  for (; o !== n; ) {
    const f = o.nextSibling;
    o.remove(), o = f;
  }
};
/**
 * @license
 * Copyright 2017 Google LLC
 * SPDX-License-Identifier: BSD-3-Clause
 */
const _ = (e, o, n) => {
  const f = /* @__PURE__ */ new Map();
  for (let r = o; r <= n; r++) f.set(e[r], r);
  return f;
}, z = T(class extends R {
  constructor(e) {
    if (super(e), e.type !== k.CHILD) throw Error("repeat() can only be used in text expressions");
  }
  dt(e, o, n) {
    let f;
    n === void 0 ? n = o : o !== void 0 && (f = o);
    const r = [], s = [];
    let t = 0;
    for (const c of e) r[t] = f ? f(c, t) : t, s[t] = n(c, t), t++;
    return { values: s, keys: r };
  }
  render(e, o, n) {
    return this.dt(e, o, n).values;
  }
  update(e, [o, n, f]) {
    const r = N(e), { values: s, keys: t } = this.dt(o, n, f);
    if (!Array.isArray(r)) return this.ut = t, s;
    const c = this.ut ??= [], u = [];
    let g, m, l = 0, a = r.length - 1, i = 0, p = s.length - 1;
    for (; l <= a && i <= p; ) if (r[l] === null) l++;
    else if (r[a] === null) a--;
    else if (c[l] === t[i]) u[i] = v(r[l], s[i]), l++, i++;
    else if (c[a] === t[p]) u[p] = v(r[a], s[p]), a--, p--;
    else if (c[l] === t[p]) u[p] = v(r[l], s[p]), b(e, u[p + 1], r[l]), l++, p--;
    else if (c[a] === t[i]) u[i] = v(r[a], s[i]), b(e, r[l], r[a]), a--, i++;
    else if (g === void 0 && (g = _(t, i, p), m = _(c, l, a)), g.has(c[l])) if (g.has(c[a])) {
      const d = m.get(t[i]), y = d !== void 0 ? r[d] : null;
      if (y === null) {
        const h = b(e, r[l]);
        v(h, s[i]), u[i] = h;
      } else u[i] = v(y, s[i]), b(e, r[l], y), r[d] = null;
      i++;
    } else $(r[a]), a--;
    else $(r[l]), l++;
    for (; i <= p; ) {
      const d = b(e, u[p + 1]);
      v(d, s[i]), u[i++] = d;
    }
    for (; l <= a; ) {
      const d = r[l++];
      d !== null && $(d);
    }
    return this.ut = t, H(e, u), U;
  }
});
export {
  F as a,
  z as c,
  L as f
};
