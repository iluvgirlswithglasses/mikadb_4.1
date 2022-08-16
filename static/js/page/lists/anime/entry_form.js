
// before include this file
// state_change_handler.js MUST BE INCLUDED FIRST

class EntryForm {
    // @constructor
    constructor () {
        let prefix = '.entry-form .field-container .field.field-attr-';
        // tools
        this.stateChangeHandler = new StateChangeHandler(this);
        // seen-related
        this.stateF = $(prefix + 'state');
        this.seenF = $(prefix + 'seen');
        this.episodesF = $(prefix + 'episodes');
        this.progressV = $(prefix + 'progress .prog');
        this.progressBtn = $(prefix + 'progress .addition-button');
        this.ratingF = $(prefix + 'rating');
        this.rewatchNotesF = $(prefix + 'rewatch-notes');
        // date-related
        this.completeDate = [
            $(prefix + 'complete-date-year'),
            $(prefix + 'complete-date-season'),
            $(prefix + 'complete-date-month'),
            $(prefix + 'complete-date-day'),
        ];
        this.startDate = [
            $(prefix + 'start-date-year'),
            $(prefix + 'start-date-season'),
            $(prefix + 'start-date-month'),
            $(prefix + 'start-date-day'),
        ];
        // date listenters
        for (let i = 0; i < 4; i++) {
            this.completeDate[i].on("change", () => this.dateValidate(this.completeDate));
            this.startDate[i].on("change", () => this.dateValidate(this.startDate));
        }
        // other listeners
        this.stateF.change(() => this.stateOnChange());
        this.seenF.change(() => this.seenOnChange());
        this.episodesF.change(() => this.episodesOnChange());
        this.progressBtn.on('click', () => this.progressBtnOnClick())
    }

    // @ajax
    dateValidate(arr) {
        $.ajax({
            url: '/ajax/SeasonalDateValidator/',
            data: {
                year: arr[0].val(),
                season: arr[1].val(),
                month: arr[2].val(),
                day: arr[3].val(),
            },
            success: (response) => {
                let regex = /(.*):(.*):(.*):(.*)/,
                    res = regex.exec(response);
                for (let i = 0; i < 4; i++) {
                    arr[i].val("" + res[i+1]);
                }
            }
        });
    }

    // @date-tools
    setSpecialDate(arr, val) {
        for (let i = 0; i < 4; i++) arr[i].val(val);
    }

    insertToday(arr) {
        let date = new Date();
        arr[0].val(date.getFullYear());   // year
        arr[2].val(date.getMonth());      // month
        arr[3].val(date.getDate());       // day
        // season
        arr[1].val(
            Math.floor(date.getMonth() / 3)
        );
    }

    // @listeners
    // state
    stateOnChange() {
        switch (Number(this.stateF.val())) {
            case 0:
            case 1:
                // watching
                this.stateChangeHandler.onWatching();
                break;
            case 2:
                // rewatching
                // nothing to do here
                break;
            case 3:
                // enqueued
                this.stateChangeHandler.onEnqueued();
                break;
            case 4:
                // complete
                this.stateChangeHandler.onComplete();
                break;
            case 5:
                // low priority
                this.stateChangeHandler.onLowPriority();
                break;
            case 6:
                // endless
                this.stateChangeHandler.onEndless();
                break;
            case 7:
                // dropped
                this.stateChangeHandler.onDropped();
                break;
            case 8:
                // plan to watch
                this.stateChangeHandler.onPlan();
                break;
        }
    }

    // seen
    // seenF will modify itself, stateF progressV
    seenOnChange() {
        // progress handler
        let prog = this.calcProgress(),
            eps = Number(this.episodesF.val());

        this.progressV.text(
            prog + ' / ' + eps
        );

        // changes state
        if (prog === eps) {
            // set state as completed
            this.stateF.val("4");
            this.stateChangeHandler.onComplete();
        } else {
            // there is change, but not completed -> watching
            let seen = Number(this.seenF.val());
            if (seen < eps) {
                if (seen > 0) {
                    // set as watching
                    if (Number(this.stateF.val()) > 1) {
                        // 0: roaming; 1: watching; greater = others;
                        this.stateF.val("1");
                    }
                    this.stateChangeHandler.onWatching();
                }
            } else {
                // completed before and currently watching again
                this.stateF.val("2");
            }
        }
    }

    // eps
    episodesOnChange() {
        // just recalculate watching statistics
        this.seenOnChange();
    }

    // progress
    // progress will modify seenF
    progressBtnOnClick() {
        // seen handler
        let newSeen = Number(this.seenF.val()) + 1;
        this.seenF.val(
            newSeen
        );
        // call other listeners
        this.seenOnChange();
    }

    // utils
    calcProgress() {
        let eps = Number(this.episodesF.val()),
            seen = Number(this.seenF.val());
        // exclude this first
        if (eps === 0) return 0;
        // no progress yet
        if (seen === 0) return 0;
        // has completed
        if (seen % eps === 0) {
            return eps;
        }
        // in progress
        return seen % eps;
    }
}