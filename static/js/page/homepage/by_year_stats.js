
$(document).ready(() => {
    new ByYearStatistics($('#by-year-statistic'), $('#by-year-statistic-select'));
});

class ByYearStatistics {
    constructor(view, selectbox) {
        this.view = view;
        this.selectbox = selectbox;
        // init
        this.getAllSect().hide();
        this.getSect(0).show();
        // event listeners
        this.selectbox.on('change', () => {
            this.getAllSect().hide();
            this.getSect(this.selectbox.val()).show();
        });
    }

    getAllSect() {
        return this.view.find('.sect');
    }

    getSect(i) {
        return this.view.find('.sect-' + i);
    }
}
