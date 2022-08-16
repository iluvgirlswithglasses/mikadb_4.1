
$(document).ready(() => {
    let byYearGroups = document.getElementById('by-year-statistic').getElementsByClassName('sect');
    for (let i = 0; i < byYearGroups.length; i++) {
        new ChartGroup(byYearGroups[i]);
    }
});

class ChartGroup {
    constructor(parent) {
        this.charts = parent.getElementsByClassName('line-chart-overflow');
        this.init();
    }

    init() {
        // let the user see the most current data
        if (this.charts.length > 0) this.scrollTo(this.charts[0].scrollWidth);
        // if one bar chart move, others will
        for (let i = 0; i < this.charts.length; i++) {
            this.charts[i].addEventListener("scroll", (event) => {
                this.scrollWith(event.target);
            });
        }
    }

    scrollTo(pos) {
        for (let i = 0; i < this.charts.length; i++) {
            this.charts[i].scrollLeft = pos;
        }
    }

    scrollWith(src) {
        for (let i = 0; i < this.charts.length; i++) {
            if (!this.charts[i].isSameNode(src)) {
                this.charts[i].scrollLeft = src.scrollLeft;
            }
        }
    }
}
