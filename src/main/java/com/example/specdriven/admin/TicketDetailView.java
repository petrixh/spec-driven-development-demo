package com.example.specdriven.admin;

import com.example.specdriven.domain.Comment;
import com.example.specdriven.domain.Status;
import com.example.specdriven.domain.Ticket;
import com.example.specdriven.ticket.TicketService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Route(value = "admin/ticket/:id", layout = AdminLayout.class)
@PageTitle("Ticket Detail | re:solve")
@RolesAllowed("ADMIN")
public class TicketDetailView extends VerticalLayout implements BeforeEnterObserver {

    private final TicketService ticketService;
    private Long ticketId;

    private final H2 titleHeading = new H2();
    private final Div detailsPanel = new Div();
    private final Select<Status> statusSelect = new Select<>();
    private final VerticalLayout commentsSection = new VerticalLayout();
    final TextArea commentInput = new TextArea();
    private final Button addCommentBtn = new Button("Add Comment");

    public TicketDetailView(TicketService ticketService) {
        this.ticketService = ticketService;

        addClassName("admin-content");
        setPadding(true);

        Button backBtn = new Button("\u2190 Back to Queue",
                e -> getUI().ifPresent(ui -> ui.navigate("admin/queue")));
        backBtn.setThemeName("tertiary");
        add(backBtn);

        add(titleHeading);

        detailsPanel.getStyle()
                .set("border", "1px solid var(--vaadin-contrast-10pct, #e2e8f0)")
                .set("border-radius", "var(--vaadin-radius-m, 8px)")
                .set("padding", "var(--vaadin-space-m)")
                .set("display", "grid")
                .set("grid-template-columns", "repeat(auto-fit, minmax(150px, 1fr))")
                .set("gap", "var(--vaadin-space-m)");
        add(detailsPanel);

        // Status change
        statusSelect.setLabel("Change Status");
        statusSelect.addValueChangeListener(e -> {
            if (e.isFromClient() && e.getValue() != null && ticketId != null) {
                try {
                    ticketService.changeStatus(ticketId, e.getValue());
                    Notification.show("Status updated", 3000, Notification.Position.TOP_CENTER);
                    refreshView();
                } catch (IllegalStateException ex) {
                    Notification.show(ex.getMessage(), 5000, Notification.Position.TOP_CENTER);
                    refreshView();
                }
            }
        });

        // Comments
        H3 commentsTitle = new H3("Comments");
        commentsSection.setPadding(false);
        commentsSection.setSpacing(true);

        commentInput.setPlaceholder("Write a comment...");
        commentInput.setWidthFull();
        commentInput.setMinHeight("100px");

        addCommentBtn.setThemeName("primary");
        addCommentBtn.addClickListener(e -> {
            String text = commentInput.getValue();
            if (text == null || text.isBlank()) {
                Notification.show("Comment cannot be empty", 3000, Notification.Position.TOP_CENTER);
                return;
            }
            try {
                ticketService.addComment(ticketId, text);
                commentInput.clear();
                refreshView();
                Notification.show("Comment added", 3000, Notification.Position.TOP_CENTER);
            } catch (IllegalStateException ex) {
                Notification.show(ex.getMessage(), 5000, Notification.Position.TOP_CENTER);
            }
        });

        HorizontalLayout commentControls = new HorizontalLayout(statusSelect);
        commentControls.setAlignItems(Alignment.BASELINE);

        add(commentControls, commentsTitle, commentsSection, commentInput, addCommentBtn);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String idParam = event.getRouteParameters().get("id").orElse(null);
        if (idParam == null) {
            event.forwardTo(QueueView.class);
            return;
        }
        try {
            ticketId = Long.parseLong(idParam);
            refreshView();
        } catch (NumberFormatException e) {
            event.forwardTo(QueueView.class);
        }
    }

    private void refreshView() {
        Ticket ticket = ticketService.getTicketById(ticketId);
        List<Comment> comments = ticketService.getComments(ticketId);

        titleHeading.setText(ticket.getTitle());

        // Details panel
        detailsPanel.removeAll();
        addDetailField("Status", createStatusBadge(ticket.getStatus()));
        addDetailField("Priority", createPriorityBadge(ticket));
        addDetailField("Category", new Span(ticket.getCategory().name()));
        addDetailField("Created By", new Span(ticket.getCreatedBy().getName()));
        addDetailField("Assigned To", new Span(
                ticket.getAssignedTo() != null ? ticket.getAssignedTo().getName() : "Unassigned"));
        addDetailField("Created",
                new Span(ticket.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));

        // Description
        Div descSection = new Div();
        descSection.add(new H3("Description"));
        Paragraph descText = new Paragraph(ticket.getDescription());
        descText.getStyle().set("white-space", "pre-wrap");
        descSection.add(descText);

        // Insert description after details panel (remove existing if present)
        getChildren()
                .filter(c -> c.getId().orElse("").equals("desc-section"))
                .findFirst()
                .ifPresent(this::remove);
        descSection.setId("desc-section");
        addComponentAtIndex(findIndex(detailsPanel) + 1, descSection);

        // Status dropdown
        Set<Status> validStatuses = ticketService.getValidNextStatuses(ticket.getStatus());
        if (validStatuses.isEmpty()) {
            statusSelect.setEnabled(false);
            statusSelect.setItems(ticket.getStatus());
            statusSelect.setValue(ticket.getStatus());
        } else {
            statusSelect.setEnabled(true);
            statusSelect.setItems(validStatuses);
            statusSelect.setValue(null);
            statusSelect.setPlaceholder(ticket.getStatus().name().replace("_", " "));
        }

        // Disable comment input for closed tickets
        boolean isClosed = ticket.getStatus() == Status.CLOSED;
        commentInput.setEnabled(!isClosed);
        addCommentBtn.setEnabled(!isClosed);

        // Comments list
        commentsSection.removeAll();
        if (comments.isEmpty()) {
            commentsSection.add(new Paragraph("No comments yet."));
        } else {
            for (Comment comment : comments) {
                Div commentDiv = new Div();
                commentDiv.addClassName("comment-item");

                Div meta = new Div();
                meta.addClassName("comment-meta");
                Span author = new Span(comment.getAuthor().getName());
                author.getStyle().set("font-weight", "600");
                Span date = new Span(" \u00B7 " + comment.getCreatedDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                meta.add(author, date);

                Paragraph text = new Paragraph(comment.getText());
                text.getStyle().set("margin", "var(--vaadin-space-xs) 0 0");

                commentDiv.add(meta, text);
                commentsSection.add(commentDiv);
            }
        }
    }

    private void addDetailField(String label, com.vaadin.flow.component.Component value) {
        Div field = new Div();
        Div labelDiv = new Div();
        labelDiv.setText(label);
        labelDiv.getStyle()
                .set("font-size", "var(--aura-font-size-s)")
                .set("color", "var(--vaadin-secondary-text-color)");
        field.add(labelDiv, value);
        detailsPanel.add(field);
    }

    private Span createStatusBadge(Status status) {
        Span badge = new Span(status.name().replace("_", " "));
        badge.addClassNames("badge", "badge-" + status.name().toLowerCase().replace("_", "-"));
        return badge;
    }

    private Span createPriorityBadge(Ticket ticket) {
        Span badge = new Span(ticket.getPriority().name());
        badge.addClassNames("badge", "badge-" + ticket.getPriority().name().toLowerCase());
        return badge;
    }

    private int findIndex(com.vaadin.flow.component.Component component) {
        var children = getChildren().toList();
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i) == component) return i;
        }
        return children.size() - 1;
    }
}
