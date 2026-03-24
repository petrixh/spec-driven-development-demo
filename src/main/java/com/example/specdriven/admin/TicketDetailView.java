package com.example.specdriven.admin;

import com.example.specdriven.data.*;
import com.example.specdriven.ticket.TicketService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Route(value = "admin/ticket", layout = AdminLayout.class)
@PageTitle("Ticket Detail — re:solve")
@RolesAllowed("ADMIN")
public class TicketDetailView extends VerticalLayout implements HasUrlParameter<Long> {

    private static final Map<Status, String> STATUS_COLORS = Map.of(
            Status.OPEN, "var(--resolve-status-open)",
            Status.IN_PROGRESS, "var(--resolve-status-in-progress)",
            Status.RESOLVED, "var(--resolve-status-resolved)",
            Status.CLOSED, "var(--resolve-status-closed)"
    );

    private final TicketService ticketService;
    private final UserRepository userRepository;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private Ticket ticket;
    private final VerticalLayout commentsSection = new VerticalLayout();
    private final TextArea commentInput = new TextArea("Add a comment");
    private final Select<Status> statusSelect = new Select<>();
    private final Span statusBadge = new Span();

    public TicketDetailView(TicketService ticketService, UserRepository userRepository) {
        this.ticketService = ticketService;
        this.userRepository = userRepository;
        setPadding(true);
        setSpacing(true);
    }

    @Override
    public void setParameter(BeforeEvent event, Long ticketId) {
        ticket = ticketService.getTicketById(ticketId).orElse(null);
        if (ticket == null) {
            event.forwardTo(TicketQueueView.class);
            return;
        }
        buildView();
    }

    private void buildView() {
        removeAll();

        Button backBtn = new Button("← Back to Queue", e ->
                getUI().ifPresent(ui -> ui.navigate(TicketQueueView.class)));
        backBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        add(backBtn);

        // Ticket details card
        VerticalLayout detailsCard = new VerticalLayout();
        detailsCard.addClassName("resolve-card");

        HorizontalLayout titleRow = new HorizontalLayout();
        titleRow.setWidthFull();
        titleRow.setJustifyContentMode(JustifyContentMode.BETWEEN);
        titleRow.setAlignItems(Alignment.CENTER);

        H2 title = new H2(ticket.getTitle());
        title.getStyle().set("margin", "0");

        // Status controls
        HorizontalLayout statusControls = new HorizontalLayout();
        statusControls.setAlignItems(Alignment.BASELINE);

        boolean isClosed = ticket.getStatus() == Status.CLOSED;
        List<Status> validStatuses = ticketService.getValidNextStatuses(ticket.getStatus());

        if (!isClosed && !validStatuses.isEmpty()) {
            statusSelect.setLabel("Change status");
            statusSelect.setItems(validStatuses);
            statusSelect.setItemLabelGenerator(s -> s.name().replace("_", " "));

            Button changeBtn = new Button("Update", e -> {
                if (statusSelect.getValue() != null) {
                    User agent = getCurrentUser();
                    ticketService.updateStatus(ticket.getId(), statusSelect.getValue(), agent);
                    Notification.show("Status updated", 3000, Notification.Position.TOP_CENTER);
                    getUI().ifPresent(ui -> ui.navigate("admin/ticket/" + ticket.getId()));
                }
            });
            changeBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
            statusControls.add(statusSelect, changeBtn);
        }

        updateStatusBadge();
        titleRow.add(title, statusBadge);
        detailsCard.add(titleRow);

        if (statusControls.getComponentCount() > 0) {
            detailsCard.add(statusControls);
        }

        // Metadata
        HorizontalLayout meta = new HorizontalLayout();
        meta.addClassName("resolve-meta");
        meta.add(
                new Span("Category: " + ticket.getCategory()),
                new Span("Priority: " + ticket.getPriority()),
                new Span("Created by: " + ticket.getCreatedBy().getName()),
                new Span("Assigned to: " + (ticket.getAssignedTo() != null ? ticket.getAssignedTo().getName() : "—")),
                new Span("Created: " + ticket.getCreatedDate().format(fmt))
        );
        detailsCard.add(meta);

        Paragraph desc = new Paragraph(ticket.getDescription());
        desc.getStyle().set("line-height", "1.6");
        detailsCard.add(desc);
        add(detailsCard);

        // Comments section
        add(new H3("Comments"));
        commentsSection.setPadding(false);
        commentsSection.setSpacing(true);
        refreshComments();
        add(commentsSection);

        // Comment input (only if not closed)
        if (!isClosed) {
            commentInput.setWidthFull();
            commentInput.setMinHeight("100px");
            commentInput.setPlaceholder("Type your comment...");

            Button addCommentBtn = new Button("Add Comment", e -> addComment());
            addCommentBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

            add(commentInput, addCommentBtn);
        }
    }

    private void addComment() {
        String text = commentInput.getValue().trim();
        if (text.isEmpty()) {
            commentInput.setInvalid(true);
            commentInput.setErrorMessage("Comment cannot be empty");
            return;
        }
        commentInput.setInvalid(false);
        User agent = getCurrentUser();
        ticketService.addComment(ticket.getId(), text, agent);
        commentInput.clear();
        refreshComments();
        Notification.show("Comment added", 3000, Notification.Position.TOP_CENTER);
    }

    private void refreshComments() {
        commentsSection.removeAll();
        List<Comment> comments = ticketService.getComments(ticket.getId());
        if (comments.isEmpty()) {
            Span empty = new Span("No comments yet.");
            empty.addClassName("resolve-meta");
            commentsSection.add(empty);
        } else {
            for (Comment c : comments) {
                VerticalLayout commentCard = new VerticalLayout();
                commentCard.addClassName("resolve-card");
                commentCard.setSpacing(false);

                HorizontalLayout commentHeader = new HorizontalLayout();
                commentHeader.setWidthFull();
                commentHeader.setJustifyContentMode(JustifyContentMode.BETWEEN);
                commentHeader.addClassName("resolve-meta");

                Span author = new Span(c.getAuthor().getName());
                author.getStyle().set("font-weight", "600");
                commentHeader.add(author, new Span(c.getCreatedDate().format(fmt)));

                Paragraph commentText = new Paragraph(c.getText());
                commentText.getStyle().set("margin", "var(--vaadin-gap-xs) 0 0 0");

                commentCard.add(commentHeader, commentText);
                commentsSection.add(commentCard);
            }
        }
    }

    private void updateStatusBadge() {
        String statusText = ticket.getStatus().name().replace("_", " ");
        statusBadge.setText(statusText);
        statusBadge.addClassName("resolve-badge");

        String color = STATUS_COLORS.getOrDefault(ticket.getStatus(), "var(--resolve-status-closed)");
        statusBadge.getStyle().set("color", color).set("background",
                "color-mix(in srgb, " + color + " 12%, transparent)");
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow();
    }
}
