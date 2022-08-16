
class UtilsBar {

    /**
     *  constructors
     * */
    constructor(filter, sortby, statefilt, ratingfilt) {
        this.init(filter, sortby, statefilt, ratingfilt);
        // listenters
        this.setUtilsBarOptionListenters('sort');
        this.setUtilsBarOptionListenters('statefilt');
        this.setUtilsBarOptionListenters('ratingfilt');
        $(document).keydown((event) => {
            if (event.keyCode === 13) this.redirect();
        });
    }

    setUtilsBarOptionListenters(s) {
        s = this.getOption(s);
        $(s).on('click', (event) => {
            $(s + '.selected').removeClass('selected');
            $(event.target).addClass('selected');
            // href
            this.redirect()
        });
    }

    redirect() {
        this.filter = $('#utils-bar .util-search input').val();
        this.sortby = $(this.getSelected('sort')).attr('value');
        this.statefilt = $(this.getSelected('statefilt')).attr('value');
        this.ratingfilt = $(this.getSelected('ratingfilt')).attr('value');
        window.location.href = page.stringPath 
        + `/query/:${ this.filter }/${ this.sortby }/${ this.statefilt }/${ this.ratingfilt }`;
    }


    /**
     *  (re)initializers
     * */
    init(filter, sortby, statefilt, ratingfilt) {
        // assign
        this.filter = filter;
        this.sortby = sortby;
        this.statefilt = statefilt;
        this.ratingfilt = ratingfilt;
        // (re)init
        this.updateUtilsBar();
    }


    /**
     *  utils
     * */
    getSelected(s) {
        return this.getOption(s) + '.selected';
    }

    getOption(s) {
        return '#utils-bar .util-' + s + ' .options-list .option';
    }


    /**
     *  #utils-bar update
     * */
    updateUtilsBar() {
        // filter
        $('#utils-bar .util-search input').val(this.filter);
        // options
        this.highlightUtilsBarOption(
            $(`#utils-bar .util-sort .options-list .option.option-${ this.sortby }`)
        );
        this.highlightUtilsBarOption(
            $(`#utils-bar .util-statefilt .options-list .option.option-${ this.statefilt }`)
        );
        this.highlightUtilsBarOption(
            $(`#utils-bar .util-ratingfilt .options-list .option.option-${ this.ratingfilt }`)
        )
    }

    highlightUtilsBarOption(opt) {
        opt.addClass('selected');
        opt.css({
            'background-color': 'var(--font-color-em)',
            'color': 'var(--font-color-bright)',
            'letter-spacing': '0.125rem',
        });
    }
}
