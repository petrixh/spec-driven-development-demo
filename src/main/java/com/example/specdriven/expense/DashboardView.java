package com.example.specdriven.expense;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.PlotOptionsPie;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

@Route("dashboard")
@PageTitle("Dashboard — GreenLedger")
@RolesAllowed("MANAGER")
public class DashboardView extends VerticalLayout {

    private final ExpenseService expenseService;

    public DashboardView(ExpenseService expenseService) {
        this.expenseService = expenseService;

        setPadding(true);

        H2 title = new H2("Dashboard");
        title.addClassName("view-title");

        HorizontalLayout cards = createCards();
        Chart chart = createCategoryChart();

        add(title, cards, chart);
    }

    private HorizontalLayout createCards() {
        BigDecimal totalPending = expenseService.getTotalPendingAmount();
        BigDecimal approvedThisMonth = expenseService.getApprovedAmountThisMonth();
        long pendingCount = expenseService.getPendingCount();

        Div pendingCard = createCard("Total Pending", formatCurrency(totalPending));
        Div approvedCard = createCard("Approved This Month", formatCurrency(approvedThisMonth));
        Div countCard = createCard("Pending Expenses", String.valueOf(pendingCount));

        HorizontalLayout layout = new HorizontalLayout(pendingCard, approvedCard, countCard);
        layout.setWidthFull();
        layout.setFlexGrow(1, pendingCard, approvedCard, countCard);
        return layout;
    }

    private Div createCard(String label, String value) {
        H3 cardLabel = new H3(label);
        Span cardValue = new Span(value);
        cardValue.addClassName("card-value");

        Div card = new Div(cardLabel, cardValue);
        card.addClassName("dashboard-card");
        return card;
    }

    private Chart createCategoryChart() {
        Chart chart = new Chart(ChartType.PIE);
        Configuration conf = chart.getConfiguration();
        conf.setTitle("Approved Expenses by Category");

        // Brand-aligned chart color palette
        SolidColor[] brandColors = {
                new SolidColor("#2E7D32"),  // --primary
                new SolidColor("#4CAF50"),  // --primary-light
                new SolidColor("#1B5E20"),  // --primary-dark
                new SolidColor("#81C784"),  // light green
                new SolidColor("#388E3C"),  // --success
        };
        conf.getChart().setBackgroundColor(new SolidColor(0, 0, 0, 0));

        DataSeries series = new DataSeries("Amount");
        Map<ExpenseCategory, BigDecimal> data = expenseService.getApprovedByCategory();

        int colorIdx = 0;
        for (Map.Entry<ExpenseCategory, BigDecimal> entry : data.entrySet()) {
            String label = switch (entry.getKey()) {
                case TRAVEL -> "Travel";
                case MEALS -> "Meals";
                case OFFICE_SUPPLIES -> "Office Supplies";
                case OTHER -> "Other";
            };
            DataSeriesItem item = new DataSeriesItem(label, entry.getValue().doubleValue());
            item.setColor(brandColors[colorIdx % brandColors.length]);
            colorIdx++;
            series.add(item);
        }

        if (series.getData().isEmpty()) {
            series.add(new DataSeriesItem("No data", 0));
        }

        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setShowInLegend(true);
        series.setPlotOptions(plotOptions);

        conf.addSeries(series);
        chart.setHeight("400px");
        return chart;
    }

    private String formatCurrency(BigDecimal amount) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(amount);
    }
}
