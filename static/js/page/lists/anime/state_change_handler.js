//@ const
const UNKNOWN = -2, WATCHING = 9999;

// @
class StateChangeHandler {
    constructor(form) {
        this.form = form;
        this.months = [
            "Jan", "Feb", "Mar", "Apr",
            "May", "Jun", "Jul", "Aug",
            "Sep", "Oct", "Nov", "Dec",
        ];
    }

    onWatching() {
        this.form.setSpecialDate(this.form.completeDate, WATCHING);
        if (this.form.seenF.val() === '1') {
            this.form.insertToday(this.form.startDate);
        }
        // 12 == N/A rating code
        if (this.form.ratingF.val() == "" + 12) {
            // 11 == currently watching rating code
            this.form.ratingF.val('' + 11);
        }
    }

    onEnqueued() {
        this.form.setSpecialDate(this.form.startDate, WATCHING);
        this.form.setSpecialDate(this.form.completeDate, WATCHING);
    }

    onComplete() {
        // update seen and progress to a valid complete statistic
        let seen = Number(this.form.seenF.val()),
            eps = Number(this.form.episodesF.val());
        // new seen value
        seen = Math.max(Math.ceil(seen / eps) * eps, eps);
        // display new seen value
        this.form.seenF.val(seen);
        this.form.progressV.text(eps + ' / ' + eps);
        // if complete for first time --> submit finished date
        if (seen === eps) {
            this.form.insertToday(this.form.completeDate);
        } else {
            // if this is not the first time --> submit rewatch note
            this.insertRewatchDate();
        }
        // the case where start date and complete date need to be set simultaneously
        // e.g. watch a movie or OVA, which have only one ep
        // there is no watching state with seen count > 0
        if (seen === 1) {
            this.form.insertToday(this.form.startDate);
        }
    }

    onLowPriority() {
        // -2 == unknown date code
        this.form.setSpecialDate(this.form.completeDate, UNKNOWN);
        // 12 == N/A rating code
        this.form.ratingF.val('' + 12);
    }

    onEndless() {
        // -2 == unknown date code
        this.form.setSpecialDate(this.form.completeDate, UNKNOWN);
        // 12 == N/A rating code
        this.form.ratingF.val('' + 12);
    }

    onDropped() {
        // -2 == unknown date code
        this.form.setSpecialDate(this.form.completeDate, UNKNOWN);
        // 12 == N/A rating code
        this.form.ratingF.val('' + 12);
    }

    onPlan() {
        // -2 == unknown date code
        this.form.setSpecialDate(this.form.startDate, UNKNOWN);
        this.form.setSpecialDate(this.form.completeDate, UNKNOWN);
        // 12 == N/A rating code
        this.form.ratingF.val("" + 12);
    }

    //
    calcRewatch() {
        let seen = Number(this.form.seenF.val()), eps = Number(this.form.episodesF.val());
        if (eps === 0) return 0;
        let rewatches = Math.floor(seen / eps) - 1;
        //
        if (rewatches < 10 || rewatches > 20) {
            rewatches += '';
            switch (rewatches[rewatches.length - 1]) {
                case '1':
                    return rewatches + 'st';
                case '2':
                    return rewatches + 'nd';
                case '3':
                    return rewatches + 'rd';
                default:
                    return rewatches + 'th';
            }
        } else {
            // from 10 --> 20, every prefix is th
            return rewatches + 'th';
        }
    }

    insertRewatchDate() {
        //
        let f = this.form.rewatchNotesF;
        let date = new Date(),
            datestr = date.getDate() + '-' + this.months[date.getMonth()] + '-' + date.getFullYear();
        let rewatches = this.calcRewatch();

        // write the rewatch statistics to the input field
        if (f.val() !== "") {
            // if the note has content in it --> break
            f.val(f.val() + '<br>');
            // if the note is empty --> don't break
        }
        // 2nd rewatch: 12-May-2021,
        f.val(f.val() + rewatches + ' rewatch: ' + datestr + ',');
    }
}
