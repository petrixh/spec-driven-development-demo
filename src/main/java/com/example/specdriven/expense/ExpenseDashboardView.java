package com.example.specdriven.expense;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.PlotOptionsPie;
import com.vaadin.flow.component.charts.model.Tooltip;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.math.BigDecimal;
import java.util.Map;

@Route("dashboard")
@PageTitle("Dashboard — GreenLedger")
@RolesAllowed("MANAGER")
public class ExpenseDashboardView extends VerticalLayout {

    private final ExpenseService expenseService;

    public ExpenseDashboardView(ExpenseService expenseService) {
        this.expenseService = expenseService;

        addClassName("view-content");

        H2 title = new H2("Dashboard");

        // Summary cards
        FlexLayout cardsLayout = new FlexLayout();
        cardsLayout.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        cardsLayout.getStyle().set("gap", "var(--vaadin-gap-l)");

        BigDecimal totalPending = expenseService.getTotalPendingAmount();
        long pendingCount = expenseService.getPendingCount();
        BigDecimal approvedThisMonth = expenseService.getApprovedAmountThisMonth();

        cardsLayout.add(
                createCard("Total Pending", String.format("$%.2f", totalPending)),
                createCard("Approved This Month", String.format("$%.2f", approvedThisMonth)),
                createCard("Pending Count", String.valueOf(pendingCount))
        );

        // Chart
        Div chartContainer = new Div();
        chartContainer.addClassName("chart-container");
        chartContainer.setWidth("100%");

        Map<Expense.Category, BigDecimal> byCategory = expenseService.getApprovedByCategory();

        if (!byCategory.isEmpty()) {
            Chart chart = new Chart(ChartType.PIE);
            Configuration conf = chart.getConfiguration();
            conf.setTitle("Approved Expenses by Category");

            Tooltip tooltip = new Tooltip();
            tooltip.setPointFormat("<b>${point.y:.2f}</b> ({point.percentage:.1f}%)");
            conf.setTooltip(tooltip);

            PlotOptionsPie plotOptions = new PlotOptionsPie();
            plotOptions.setShowInLegend(true);
            conf.setPlotOptions(plotOptions);

            DataSeries series = new DataSeries("Amount");
            for (Map.Entry<Expense.Category, BigDecimal> entry : byCategory.entrySet()) {
                series.add(new DataSeriesItem(
                        entry.getKey().getLabel(),
                        entry.getValue().doubleValue()));
            }
            conf.addSeries(series);

            chart.setWidth("100%");
            chart.setHeight("400px");
            chartContainer.add(chart);
        } else {
            chartContainer.add(new Paragraph("No approved expenses yet."));
        }

        add(title, cardsLayout, chartContainer);
    }

    private Div createCard(String label, String value) {
        Div card = new Div();
        card.addClassName("summary-card");

        Paragraph cardTitle = new Paragraph(label);
        cardTitle.addClassName("card-title");

        Paragraph cardValue = new Paragraph(value);
        cardValue.addClassName("card-value");

        card.add(cardTitle, cardValue);
        return card;
    }
}
