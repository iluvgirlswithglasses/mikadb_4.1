$(document).ready(() => {
	new YearlyStatsSelectBox($('#by-year-stats'), $('#by-year-stats-select'))
});

class YearlyStatsSelectBox {
	constructor(view, selectbox) {
		this.view = view;
		this.selectbox = selectbox;
		// init
		let yr = new Date().getFullYear();
		this.getAllSect().hide();
		this.getSect(yr).show();
		this.selectbox.val(yr);
		// event listener
        this.selectbox.on('change', () => {
            this.getAllSect().hide();
            this.getSect(this.selectbox.val()).show();
        });
	}

    getAllSect() {
        return this.view.find('.year-stats-container');
    }

    getSect(i) {
        return this.view.find('.year-stats-' + i);
    }
}
