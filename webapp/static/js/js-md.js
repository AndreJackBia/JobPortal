! function() {
    "use strict";

    function e(e, t) {
        if (e) {
            if (t.element_.classList.contains(t.CssClasses_.MDL_JS_RIPPLE_EFFECT)) {
                var s = document.createElement("span");
                s.classList.add(t.CssClasses_.MDL_RIPPLE_CONTAINER), s.classList.add(t.CssClasses_.MDL_JS_RIPPLE_EFFECT);
                var i = document.createElement("span");
                i.classList.add(t.CssClasses_.MDL_RIPPLE), s.appendChild(i), e.appendChild(s)
            }
            e.addEventListener("click", function(s) {
                if ("#" === e.getAttribute("href").charAt(0)) {
                    s.preventDefault();
                    var i = e.href.split("#")[1],
                        n = t.element_.querySelector("#" + i);
                    t.resetTabState_(), t.resetPanelState_(), e.classList.add(t.CssClasses_.ACTIVE_CLASS), n.classList.add(t.CssClasses_.ACTIVE_CLASS)
                }
            })
        }
    }

    function t(e, t, s, i) {
        function n() {
            var n = e.href.split("#")[1],
                a = i.content_.querySelector("#" + n);
            i.resetTabState_(t), i.resetPanelState_(s), e.classList.add(i.CssClasses_.IS_ACTIVE), a.classList.add(i.CssClasses_.IS_ACTIVE)
        }
        if (i.tabBar_.classList.contains(i.CssClasses_.JS_RIPPLE_EFFECT)) {
            var a = document.createElement("span");
            a.classList.add(i.CssClasses_.RIPPLE_CONTAINER), a.classList.add(i.CssClasses_.JS_RIPPLE_EFFECT);
            var l = document.createElement("span");
            l.classList.add(i.CssClasses_.RIPPLE), a.appendChild(l), e.appendChild(a)
        }
        i.tabBar_.classList.contains(i.CssClasses_.TAB_MANUAL_SWITCH) || e.addEventListener("click", function(t) {
            "#" === e.getAttribute("href").charAt(0) && (t.preventDefault(), n())
        }), e.show = n
    }
    var s = {
        upgradeDom: function(e, t) {},
        upgradeElement: function(e, t) {},
        upgradeElements: function(e) {},
        upgradeAllRegistered: function() {},
        registerUpgradedCallback: function(e, t) {},
        register: function(e) {},
        downgradeElements: function(e) {}
    };
    s = function() {
        function e(e, t) {
            for (var s = 0; s < c.length; s++)
                if (c[s].className === e) return "undefined" != typeof t && (c[s] = t), c[s];
            return !1
        }

        function t(e) {
            var t = e.getAttribute("data-upgraded");
            return null === t ? [""] : t.split(",")
        }

        function s(e, s) {
            var i = t(e);
            return i.indexOf(s) !== -1
        }

        function i(e, t, s) {
            if ("CustomEvent" in window && "function" == typeof window.CustomEvent) return new CustomEvent(e, {
                bubbles: t,
                cancelable: s
            });
            var i = document.createEvent("Events");
            return i.initEvent(e, t, s), i
        }

        function n(t, s) {
            if ("undefined" == typeof t && "undefined" == typeof s)
                for (var i = 0; i < c.length; i++) n(c[i].className, c[i].cssClass);
            else {
                var l = t;
                if ("undefined" == typeof s) {
                    var o = e(l);
                    o && (s = o.cssClass)
                }
                for (var r = document.querySelectorAll("." + s), _ = 0; _ < r.length; _++) a(r[_], l)
            }
        }

        function a(n, a) {
            if (!("object" == typeof n && n instanceof Element)) throw new Error("Invalid argument provided to upgrade MDL element.");
            var l = i("mdl-componentupgrading", !0, !0);
            if (n.dispatchEvent(l), !l.defaultPrevented) {
                var o = t(n),
                    r = [];
                if (a) s(n, a) || r.push(e(a));
                else {
                    var _ = n.classList;
                    c.forEach(function(e) {
                        _.contains(e.cssClass) && r.indexOf(e) === -1 && !s(n, e.className) && r.push(e)
                    })
                }
                for (var d, h = 0, u = r.length; h < u; h++) {
                    if (d = r[h], !d) throw new Error("Unable to find a registered component for the given class.");
                    o.push(d.className), n.setAttribute("data-upgraded", o.join(","));
                    var E = new d.classConstructor(n);
                    E[C] = d, p.push(E);
                    for (var m = 0, L = d.callbacks.length; m < L; m++) d.callbacks[m](n);
                    d.widget && (n[d.className] = E);
                    var I = i("mdl-componentupgraded", !0, !1);
                    n.dispatchEvent(I)
                }
            }
        }

        function l(e) {
            Array.isArray(e) || (e = e instanceof Element ? [e] : Array.prototype.slice.call(e));
            for (var t, s = 0, i = e.length; s < i; s++) t = e[s], t instanceof HTMLElement && (a(t), t.children.length > 0 && l(t.children))
        }

        function o(t) {
            var s = "undefined" == typeof t.widget && "undefined" == typeof t.widget,
                i = !0;
            s || (i = t.widget || t.widget);
            var n = {
                classConstructor: t.constructor || t.constructor,
                className: t.classAsString || t.classAsString,
                cssClass: t.cssClass || t.cssClass,
                widget: i,
                callbacks: []
            };
            if (c.forEach(function(e) {
                    if (e.cssClass === n.cssClass) throw new Error("The provided cssClass has already been registered: " + e.cssClass);
                    if (e.className === n.className) throw new Error("The provided className has already been registered")
                }), t.constructor.prototype.hasOwnProperty(C)) throw new Error("MDL component classes must not have " + C + " defined as a property.");
            var a = e(t.classAsString, n);
            a || c.push(n)
        }

        function r(t, s) {
            var i = e(t);
            i && i.callbacks.push(s)
        }

        function _() {
            for (var e = 0; e < c.length; e++) n(c[e].className)
        }

        function d(e) {
            if (e) {
                var t = p.indexOf(e);
                p.splice(t, 1);
                var s = e.element_.getAttribute("data-upgraded").split(","),
                    n = s.indexOf(e[C].classAsString);
                s.splice(n, 1), e.element_.setAttribute("data-upgraded", s.join(","));
                var a = i("mdl-componentdowngraded", !0, !1);
                e.element_.dispatchEvent(a)
            }
        }

        function h(e) {
            var t = function(e) {
                p.filter(function(t) {
                    return t.element_ === e
                }).forEach(d)
            };
            if (e instanceof Array || e instanceof NodeList)
                for (var s = 0; s < e.length; s++) t(e[s]);
            else {
                if (!(e instanceof Node)) throw new Error("Invalid argument provided to downgrade MDL nodes.");
                t(e)
            }
        }
        var c = [],
            p = [],
            C = "mdlComponentConfigInternal_";
        return {
            upgradeDom: n,
            upgradeElement: a,
            upgradeElements: l,
            upgradeAllRegistered: _,
            registerUpgradedCallback: r,
            register: o,
            downgradeElements: h
        }
    }(), s.ComponentConfigPublic, s.ComponentConfig, s.Component, s.upgradeDom = s.upgradeDom, s.upgradeElement = s.upgradeElement, s.upgradeElements = s.upgradeElements, s.upgradeAllRegistered = s.upgradeAllRegistered, s.registerUpgradedCallback = s.registerUpgradedCallback, s.register = s.register, s.downgradeElements = s.downgradeElements, window.componentHandler = s, window.componentHandler = s, window.addEventListener("load", function() {
        "classList" in document.createElement("div") && "querySelector" in document && "addEventListener" in window && Array.prototype.forEach ? (document.documentElement.classList.add("mdl-js"), s.upgradeAllRegistered()) : (s.upgradeElement = function() {}, s.register = function() {})
    }), Date.now || (Date.now = function() {
        return (new Date).getTime()
    }, Date.now = Date.now);
    for (var i = ["webkit", "moz"], n = 0; n < i.length && !window.requestAnimationFrame; ++n) {
        var a = i[n];
        window.requestAnimationFrame = window[a + "RequestAnimationFrame"], window.cancelAnimationFrame = window[a + "CancelAnimationFrame"] || window[a + "CancelRequestAnimationFrame"], window.requestAnimationFrame = window.requestAnimationFrame, window.cancelAnimationFrame = window.cancelAnimationFrame
    }
    if (/iP(ad|hone|od).*OS 6/.test(window.navigator.userAgent) || !window.requestAnimationFrame || !window.cancelAnimationFrame) {
        var l = 0;
        window.requestAnimationFrame = function(e) {
            var t = Date.now(),
                s = Math.max(l + 16, t);
            return setTimeout(function() {
                e(l = s)
            }, s - t)
        }, window.cancelAnimationFrame = clearTimeout, window.requestAnimationFrame = window.requestAnimationFrame, window.cancelAnimationFrame = window.cancelAnimationFrame
    }
    var o = function(e) {
        this.element_ = e, this.init()
    };
    window.MaterialButton = o, o.prototype.Constant_ = {}, o.prototype.CssClasses_ = {
        RIPPLE_EFFECT: "mdl-js-ripple-effect",
        RIPPLE_CONTAINER: "mdl-button__ripple-container",
        RIPPLE: "mdl-ripple"
    }, o.prototype.blurHandler_ = function(e) {
        e && this.element_.blur()
    }, o.prototype.disable = function() {
        this.element_.disabled = !0
    }, o.prototype.disable = o.prototype.disable, o.prototype.enable = function() {
        this.element_.disabled = !1
    }, o.prototype.enable = o.prototype.enable, o.prototype.init = function() {
        if (this.element_) {
            if (this.element_.classList.contains(this.CssClasses_.RIPPLE_EFFECT)) {
                var e = document.createElement("span");
                e.classList.add(this.CssClasses_.RIPPLE_CONTAINER), this.rippleElement_ = document.createElement("span"), this.rippleElement_.classList.add(this.CssClasses_.RIPPLE), e.appendChild(this.rippleElement_), this.boundRippleBlurHandler = this.blurHandler_.bind(this), this.rippleElement_.addEventListener("mouseup", this.boundRippleBlurHandler), this.element_.appendChild(e)
            }
            this.boundButtonBlurHandler = this.blurHandler_.bind(this), this.element_.addEventListener("mouseup", this.boundButtonBlurHandler), this.element_.addEventListener("mouseleave", this.boundButtonBlurHandler)
        }
    }, s.register({
        constructor: o,
        classAsString: "MaterialButton",
        cssClass: "mdl-js-button",
        widget: !0
    });
    var r = function(e) {
        this.element_ = e, this.init()
    };
    window.MaterialCheckbox = r, r.prototype.Constant_ = {
        TINY_TIMEOUT: .001
    }, r.prototype.CssClasses_ = {
        INPUT: "mdl-checkbox__input",
        BOX_OUTLINE: "mdl-checkbox__box-outline",
        FOCUS_HELPER: "mdl-checkbox__focus-helper",
        TICK_OUTLINE: "mdl-checkbox__tick-outline",
        RIPPLE_EFFECT: "mdl-js-ripple-effect",
        RIPPLE_IGNORE_EVENTS: "mdl-js-ripple-effect--ignore-events",
        RIPPLE_CONTAINER: "mdl-checkbox__ripple-container",
        RIPPLE_CENTER: "mdl-ripple--center",
        RIPPLE: "mdl-ripple",
        IS_FOCUSED: "is-focused",
        IS_DISABLED: "is-disabled",
        IS_CHECKED: "is-checked",
        IS_UPGRADED: "is-upgraded"
    }, r.prototype.onChange_ = function(e) {
        this.updateClasses_()
    }, r.prototype.onFocus_ = function(e) {
        this.element_.classList.add(this.CssClasses_.IS_FOCUSED)
    }, r.prototype.onBlur_ = function(e) {
        this.element_.classList.remove(this.CssClasses_.IS_FOCUSED)
    }, r.prototype.onMouseUp_ = function(e) {
        this.blur_()
    }, r.prototype.updateClasses_ = function() {
        this.checkDisabled(), this.checkToggleState()
    }, r.prototype.blur_ = function() {
        window.setTimeout(function() {
            this.inputElement_.blur()
        }.bind(this), this.Constant_.TINY_TIMEOUT)
    }, r.prototype.checkToggleState = function() {
        this.inputElement_.checked ? this.element_.classList.add(this.CssClasses_.IS_CHECKED) : this.element_.classList.remove(this.CssClasses_.IS_CHECKED)
    }, r.prototype.checkToggleState = r.prototype.checkToggleState, r.prototype.checkDisabled = function() {
        this.inputElement_.disabled ? this.element_.classList.add(this.CssClasses_.IS_DISABLED) : this.element_.classList.remove(this.CssClasses_.IS_DISABLED)
    }, r.prototype.checkDisabled = r.prototype.checkDisabled, r.prototype.disable = function() {
        this.inputElement_.disabled = !0, this.updateClasses_()
    }, r.prototype.disable = r.prototype.disable, r.prototype.enable = function() {
        this.inputElement_.disabled = !1, this.updateClasses_()
    }, r.prototype.enable = r.prototype.enable, r.prototype.check = function() {
        this.inputElement_.checked = !0, this.updateClasses_()
    }, r.prototype.check = r.prototype.check, r.prototype.uncheck = function() {
        this.inputElement_.checked = !1, this.updateClasses_()
    }, r.prototype.uncheck = r.prototype.uncheck, r.prototype.init = function() {
        if (this.element_) {
            this.inputElement_ = this.element_.querySelector("." + this.CssClasses_.INPUT);
            var e = document.createElement("span");
            e.classList.add(this.CssClasses_.BOX_OUTLINE);
            var t = document.createElement("span");
            t.classList.add(this.CssClasses_.FOCUS_HELPER);
            var s = document.createElement("span");
            if (s.classList.add(this.CssClasses_.TICK_OUTLINE), e.appendChild(s), this.element_.appendChild(t), this.element_.appendChild(e), this.element_.classList.contains(this.CssClasses_.RIPPLE_EFFECT)) {
                this.element_.classList.add(this.CssClasses_.RIPPLE_IGNORE_EVENTS), this.rippleContainerElement_ = document.createElement("span"), this.rippleContainerElement_.classList.add(this.CssClasses_.RIPPLE_CONTAINER), this.rippleContainerElement_.classList.add(this.CssClasses_.RIPPLE_EFFECT), this.rippleContainerElement_.classList.add(this.CssClasses_.RIPPLE_CENTER), this.boundRippleMouseUp = this.onMouseUp_.bind(this), this.rippleContainerElement_.addEventListener("mouseup", this.boundRippleMouseUp);
                var i = document.createElement("span");
                i.classList.add(this.CssClasses_.RIPPLE), this.rippleContainerElement_.appendChild(i), this.element_.appendChild(this.rippleContainerElement_)
            }
            this.boundInputOnChange = this.onChange_.bind(this), this.boundInputOnFocus = this.onFocus_.bind(this), this.boundInputOnBlur = this.onBlur_.bind(this), this.boundElementMouseUp = this.onMouseUp_.bind(this), this.inputElement_.addEventListener("change", this.boundInputOnChange), this.inputElement_.addEventListener("focus", this.boundInputOnFocus), this.inputElement_.addEventListener("blur", this.boundInputOnBlur), this.element_.addEventListener("mouseup", this.boundElementMouseUp), this.updateClasses_(), this.element_.classList.add(this.CssClasses_.IS_UPGRADED)
        }
    }, s.register({
        constructor: r,
        classAsString: "MaterialCheckbox",
        cssClass: "mdl-js-checkbox",
        widget: !0
    });
    var _ = function(e) {
        this.element_ = e, this.init()
    };
    window.MaterialIconToggle = _, _.prototype.Constant_ = {
        TINY_TIMEOUT: .001
    }, _.prototype.CssClasses_ = {
        INPUT: "mdl-icon-toggle__input",
        JS_RIPPLE_EFFECT: "mdl-js-ripple-effect",
        RIPPLE_IGNORE_EVENTS: "mdl-js-ripple-effect--ignore-events",
        RIPPLE_CONTAINER: "mdl-icon-toggle__ripple-container",
        RIPPLE_CENTER: "mdl-ripple--center",
        RIPPLE: "mdl-ripple",
        IS_FOCUSED: "is-focused",
        IS_DISABLED: "is-disabled",
        IS_CHECKED: "is-checked"
    }, _.prototype.onChange_ = function(e) {
        this.updateClasses_()
    }, _.prototype.onFocus_ = function(e) {
        this.element_.classList.add(this.CssClasses_.IS_FOCUSED)
    }, _.prototype.onBlur_ = function(e) {
        this.element_.classList.remove(this.CssClasses_.IS_FOCUSED)
    }, _.prototype.onMouseUp_ = function(e) {
        this.blur_()
    }, _.prototype.updateClasses_ = function() {
        this.checkDisabled(), this.checkToggleState()
    }, _.prototype.blur_ = function() {
        window.setTimeout(function() {
            this.inputElement_.blur()
        }.bind(this), this.Constant_.TINY_TIMEOUT)
    }, _.prototype.checkToggleState = function() {
        this.inputElement_.checked ? this.element_.classList.add(this.CssClasses_.IS_CHECKED) : this.element_.classList.remove(this.CssClasses_.IS_CHECKED)
    }, _.prototype.checkToggleState = _.prototype.checkToggleState, _.prototype.checkDisabled = function() {
        this.inputElement_.disabled ? this.element_.classList.add(this.CssClasses_.IS_DISABLED) : this.element_.classList.remove(this.CssClasses_.IS_DISABLED)
    }, _.prototype.checkDisabled = _.prototype.checkDisabled, _.prototype.disable = function() {
        this.inputElement_.disabled = !0, this.updateClasses_()
    }, _.prototype.disable = _.prototype.disable, _.prototype.enable = function() {
        this.inputElement_.disabled = !1, this.updateClasses_()
    }, _.prototype.enable = _.prototype.enable, _.prototype.check = function() {
        this.inputElement_.checked = !0, this.updateClasses_()
    }, _.prototype.check = _.prototype.check, _.prototype.uncheck = function() {
        this.inputElement_.checked = !1, this.updateClasses_()
    }, _.prototype.uncheck = _.prototype.uncheck, _.prototype.init = function() {
        if (this.element_) {
            if (this.inputElement_ = this.element_.querySelector("." + this.CssClasses_.INPUT), this.element_.classList.contains(this.CssClasses_.JS_RIPPLE_EFFECT)) {
                this.element_.classList.add(this.CssClasses_.RIPPLE_IGNORE_EVENTS), this.rippleContainerElement_ = document.createElement("span"), this.rippleContainerElement_.classList.add(this.CssClasses_.RIPPLE_CONTAINER), this.rippleContainerElement_.classList.add(this.CssClasses_.JS_RIPPLE_EFFECT), this.rippleContainerElement_.classList.add(this.CssClasses_.RIPPLE_CENTER), this.boundRippleMouseUp = this.onMouseUp_.bind(this), this.rippleContainerElement_.addEventListener("mouseup", this.boundRippleMouseUp);
                var e = document.createElement("span");
                e.classList.add(this.CssClasses_.RIPPLE), this.rippleContainerElement_.appendChild(e), this.element_.appendChild(this.rippleContainerElement_)
            }
            this.boundInputOnChange = this.onChange_.bind(this), this.boundInputOnFocus = this.onFocus_.bind(this), this.boundInputOnBlur = this.onBlur_.bind(this), this.boundElementOnMouseUp = this.onMouseUp_.bind(this), this.inputElement_.addEventListener("change", this.boundInputOnChange), this.inputElement_.addEventListener("focus", this.boundInputOnFocus), this.inputElement_.addEventListener("blur", this.boundInputOnBlur), this.element_.addEventListener("mouseup", this.boundElementOnMouseUp), this.updateClasses_(), this.element_.classList.add("is-upgraded")
        }
    }, s.register({
        constructor: _,
        classAsString: "MaterialIconToggle",
        cssClass: "mdl-js-icon-toggle",
        widget: !0
    });
    var d = function(e) {
        this.element_ = e, this.init()
    };
    window.MaterialMenu = d, d.prototype.Constant_ = {
        TRANSITION_DURATION_SECONDS: .3,
        TRANSITION_DURATION_FRACTION: .8,
        CLOSE_TIMEOUT: 150
    }, d.prototype.Keycodes_ = {
        ENTER: 13,
        ESCAPE: 27,
        SPACE: 32,
        UP_ARROW: 38,
        DOWN_ARROW: 40
    }, d.prototype.CssClasses_ = {
        CONTAINER: "mdl-menu__container",
        OUTLINE: "mdl-menu__outline",
        ITEM: "mdl-menu__item",
        ITEM_RIPPLE_CONTAINER: "mdl-menu__item-ripple-container",
        RIPPLE_EFFECT: "mdl-js-ripple-effect",
        RIPPLE_IGNORE_EVENTS: "mdl-js-ripple-effect--ignore-events",
        RIPPLE: "mdl-ripple",
        IS_UPGRADED: "is-upgraded",
        IS_VISIBLE: "is-visible",
        IS_ANIMATING: "is-animating",
        BOTTOM_LEFT: "mdl-menu--bottom-left",
        BOTTOM_RIGHT: "mdl-menu--bottom-right",
        TOP_LEFT: "mdl-menu--top-left",
        TOP_RIGHT: "mdl-menu--top-right",
        UNALIGNED: "mdl-menu--unaligned"
    }, d.prototype.init = function() {
        if (this.element_) {
            var e = document.createElement("div");
            e.classList.add(this.CssClasses_.CONTAINER), this.element_.parentElement.insertBefore(e, this.element_), this.element_.parentElement.removeChild(this.element_), e.appendChild(this.element_), this.container_ = e;
            var t = document.createElement("div");
            t.classList.add(this.CssClasses_.OUTLINE), this.outline_ = t, e.insertBefore(t, this.element_);
            var s = this.element_.getAttribute("for") || this.element_.getAttribute("data-mdl-for"),
                i = null;
            s && (i = document.getElementById(s), i && (this.forElement_ = i, i.addEventListener("click", this.handleForClick_.bind(this)), i.addEventListener("keydown", this.handleForKeyboardEvent_.bind(this))));
            var n = this.element_.querySelectorAll("." + this.CssClasses_.ITEM);
            this.boundItemKeydown_ = this.handleItemKeyboardEvent_.bind(this), this.boundItemClick_ = this.handleItemClick_.bind(this);
            for (var a = 0; a < n.length; a++) n[a].addEventListener("click", this.boundItemClick_), n[a].tabIndex = "-1", n[a].addEventListener("keydown", this.boundItemKeydown_);
            if (this.element_.classList.contains(this.CssClasses_.RIPPLE_EFFECT))
                for (this.element_.classList.add(this.CssClasses_.RIPPLE_IGNORE_EVENTS), a = 0; a < n.length; a++) {
                    var l = n[a],
                        o = document.createElement("span");
                    o.classList.add(this.CssClasses_.ITEM_RIPPLE_CONTAINER);
                    var r = document.createElement("span");
                    r.classList.add(this.CssClasses_.RIPPLE), o.appendChild(r), l.appendChild(o), l.classList.add(this.CssClasses_.RIPPLE_EFFECT)
                }
            this.element_.classList.contains(this.CssClasses_.BOTTOM_LEFT) && this.outline_.classList.add(this.CssClasses_.BOTTOM_LEFT), this.element_.classList.contains(this.CssClasses_.BOTTOM_RIGHT) && this.outline_.classList.add(this.CssClasses_.BOTTOM_RIGHT), this.element_.classList.contains(this.CssClasses_.TOP_LEFT) && this.outline_.classList.add(this.CssClasses_.TOP_LEFT), this.element_.classList.contains(this.CssClasses_.TOP_RIGHT) && this.outline_.classList.add(this.CssClasses_.TOP_RIGHT), this.element_.classList.contains(this.CssClasses_.UNALIGNED) && this.outline_.classList.add(this.CssClasses_.UNALIGNED), e.classList.add(this.CssClasses_.IS_UPGRADED)
        }
    }, d.prototype.handleForClick_ = function(e) {
        if (this.element_ && this.forElement_) {
            var t = this.forElement_.getBoundingClientRect(),
                s = this.forElement_.parentElement.getBoundingClientRect();
            this.element_.classList.contains(this.CssClasses_.UNALIGNED) || (this.element_.classList.contains(this.CssClasses_.BOTTOM_RIGHT) ? (this.container_.style.right = s.right - t.right + "px", this.container_.style.top = this.forElement_.offsetTop + this.forElement_.offsetHeight + "px") : this.element_.classList.contains(this.CssClasses_.TOP_LEFT) ? (this.container_.style.left = this.forElement_.offsetLeft + "px", this.container_.style.bottom = s.bottom - t.top + "px") : this.element_.classList.contains(this.CssClasses_.TOP_RIGHT) ? (this.container_.style.right = s.right - t.right + "px", this.container_.style.bottom = s.bottom - t.top + "px") : (this.container_.style.left = this.forElement_.offsetLeft + "px", this.container_.style.top = this.forElement_.offsetTop + this.forElement_.offsetHeight + "px"))
        }
        this.toggle(e)
    }, d.prototype.handleForKeyboardEvent_ = function(e) {
        if (this.element_ && this.container_ && this.forElement_) {
            var t = this.element_.querySelectorAll("." + this.CssClasses_.ITEM + ":not([disabled])");
            t && t.length > 0 && this.container_.classList.contains(this.CssClasses_.IS_VISIBLE) && (e.keyCode === this.Keycodes_.UP_ARROW ? (e.preventDefault(), t[t.length - 1].focus()) : e.keyCode === this.Keycodes_.DOWN_ARROW && (e.preventDefault(), t[0].focus()))
        }
    }, d.prototype.handleItemKeyboardEvent_ = function(e) {
        if (this.element_ && this.container_) {
            var t = this.element_.querySelectorAll("." + this.CssClasses_.ITEM + ":not([disabled])");
            if (t && t.length > 0 && this.container_.classList.contains(this.CssClasses_.IS_VISIBLE)) {
                var s = Array.prototype.slice.call(t).indexOf(e.target);
                if (e.keyCode === this.Keycodes_.UP_ARROW) e.preventDefault(), s > 0 ? t[s - 1].focus() : t[t.length - 1].focus();
                else if (e.keyCode === this.Keycodes_.DOWN_ARROW) e.preventDefault(), t.length > s + 1 ? t[s + 1].focus() : t[0].focus();
                else if (e.keyCode === this.Keycodes_.SPACE || e.keyCode === this.Keycodes_.ENTER) {
                    e.preventDefault();
                    var i = new MouseEvent("mousedown");
                    e.target.dispatchEvent(i), i = new MouseEvent("mouseup"), e.target.dispatchEvent(i), e.target.click()
                } else e.keyCode === this.Keycodes_.ESCAPE && (e.preventDefault(), this.hide())
            }
        }
    }, d.prototype.handleItemClick_ = function(e) {
        e.target.hasAttribute("disabled") ? e.stopPropagation() : (this.closing_ = !0, window.setTimeout(function(e) {
            this.hide(), this.closing_ = !1
        }.bind(this), this.Constant_.CLOSE_TIMEOUT))
    }, d.prototype.applyClip_ = function(e, t) {
        this.element_.classList.contains(this.CssClasses_.UNALIGNED) ? this.element_.style.clip = "" : this.element_.classList.contains(this.CssClasses_.BOTTOM_RIGHT) ? this.element_.style.clip = "rect(0 " + t + "px 0 " + t + "px)" : this.element_.classList.contains(this.CssClasses_.TOP_LEFT) ? this.element_.style.clip = "rect(" + e + "px 0 " + e + "px 0)" : this.element_.classList.contains(this.CssClasses_.TOP_RIGHT) ? this.element_.style.clip = "rect(" + e + "px " + t + "px " + e + "px " + t + "px)" : this.element_.style.clip = ""
    }, d.prototype.removeAnimationEndListener_ = function(e) {
        e.target.classList.remove(d.prototype.CssClasses_.IS_ANIMATING)
    }, d.prototype.addAnimationEndListener_ = function() {
        this.element_.addEventListener("transitionend", this.removeAnimationEndListener_), this.element_.addEventListener("webkitTransitionEnd", this.removeAnimationEndListener_)
    }, d.prototype.show = function(e) {
        if (this.element_ && this.container_ && this.outline_) {
            var t = this.element_.getBoundingClientRect().height,
                s = this.element_.getBoundingClientRect().width;
            this.container_.style.width = s + "px", this.container_.style.height = t + "px", this.outline_.style.width = s + "px", this.outline_.style.height = t + "px";
            for (var i = this.Constant_.TRANSITION_DURATION_SECONDS * this.Constant_.TRANSITION_DURATION_FRACTION, n = this.element_.querySelectorAll("." + this.CssClasses_.ITEM), a = 0; a < n.length; a++) {
                var l = null;
                l = this.element_.classList.contains(this.CssClasses_.TOP_LEFT) || this.element_.classList.contains(this.CssClasses_.TOP_RIGHT) ? (t - n[a].offsetTop - n[a].offsetHeight) / t * i + "s" : n[a].offsetTop / t * i + "s", n[a].style.transitionDelay = l
            }
            this.applyClip_(t, s), window.requestAnimationFrame(function() {
                this.element_.classList.add(this.CssClasses_.IS_ANIMATING), this.element_.style.clip = "rect(0 " + s + "px " + t + "px 0)", this.container_.classList.add(this.CssClasses_.IS_VISIBLE)
            }.bind(this)), this.addAnimationEndListener_();
            var o = function(t) {
                t === e || this.closing_ || t.target.parentNode === this.element_ || (document.removeEventListener("click", o), this.hide())
            }.bind(this);
            document.addEventListener("click", o)
        }
    }, d.prototype.show = d.prototype.show, d.prototype.hide = function() {
        if (this.element_ && this.container_ && this.outline_) {
            for (var e = this.element_.querySelectorAll("." + this.CssClasses_.ITEM), t = 0; t < e.length; t++) e[t].style.removeProperty("transition-delay");
            var s = this.element_.getBoundingClientRect(),
                i = s.height,
                n = s.width;
            this.element_.classList.add(this.CssClasses_.IS_ANIMATING), this.applyClip_(i, n), this.container_.classList.remove(this.CssClasses_.IS_VISIBLE), this.addAnimationEndListener_()
        }
    }, d.prototype.hide = d.prototype.hide, d.prototype.toggle = function(e) {
        this.container_.classList.contains(this.CssClasses_.IS_VISIBLE) ? this.hide() : this.show(e)
    }, d.prototype.toggle = d.prototype.toggle, s.register({
        constructor: d,
        classAsString: "MaterialMenu",
        cssClass: "mdl-js-menu",
        widget: !0
    });
    var h = function(e) {
        this.element_ = e, this.init()
    };
    window.MaterialProgress = h, h.prototype.Constant_ = {}, h.prototype.CssClasses_ = {
        INDETERMINATE_CLASS: "mdl-progress__indeterminate"
    }, h.prototype.setProgress = function(e) {
        this.element_.classList.contains(this.CssClasses_.INDETERMINATE_CLASS) || (this.progressbar_.style.width = e + "%")
    }, h.prototype.setProgress = h.prototype.setProgress, h.prototype.setBuffer = function(e) {
        this.bufferbar_.style.width = e + "%", this.auxbar_.style.width = 100 - e + "%"
    }, h.prototype.setBuffer = h.prototype.setBuffer, h.prototype.init = function() {
        if (this.element_) {
            var e = document.createElement("div");
            e.className = "progressbar bar bar1", this.element_.appendChild(e), this.progressbar_ = e, e = document.createElement("div"), e.className = "bufferbar bar bar2", this.element_.appendChild(e), this.bufferbar_ = e, e = document.createElement("div"), e.className = "auxbar bar bar3", this.element_.appendChild(e), this.auxbar_ = e, this.progressbar_.style.width = "0%", this.bufferbar_.style.width = "100%", this.auxbar_.style.width = "0%", this.element_.classList.add("is-upgraded")
        }
    }, s.register({
        constructor: h,
        classAsString: "MaterialProgress",
        cssClass: "mdl-js-progress",
        widget: !0
    });
    var c = function(e) {
        this.element_ = e, this.init()
    };
    window.MaterialRadio = c, c.prototype.Constant_ = {
        TINY_TIMEOUT: .001
    }, c.prototype.CssClasses_ = {
        IS_FOCUSED: "is-focused",
        IS_DISABLED: "is-disabled",
        IS_CHECKED: "is-checked",
        IS_UPGRADED: "is-upgraded",
        JS_RADIO: "mdl-js-radio",
        RADIO_BTN: "mdl-radio__button",
        RADIO_OUTER_CIRCLE: "mdl-radio__outer-circle",
        RADIO_INNER_CIRCLE: "mdl-radio__inner-circle",
        RIPPLE_EFFECT: "mdl-js-ripple-effect",
        RIPPLE_IGNORE_EVENTS: "mdl-js-ripple-effect--ignore-events",
        RIPPLE_CONTAINER: "mdl-radio__ripple-container",
        RIPPLE_CENTER: "mdl-ripple--center",
        RIPPLE: "mdl-ripple"
    }, c.prototype.onChange_ = function(e) {
        for (var t = document.getElementsByClassName(this.CssClasses_.JS_RADIO), s = 0; s < t.length; s++) {
            var i = t[s].querySelector("." + this.CssClasses_.RADIO_BTN);
            i.getAttribute("name") === this.btnElement_.getAttribute("name") && "undefined" != typeof t[s].MaterialRadio && t[s].MaterialRadio.updateClasses_()
        }
    }, c.prototype.onFocus_ = function(e) {
        this.element_.classList.add(this.CssClasses_.IS_FOCUSED)
    }, c.prototype.onBlur_ = function(e) {
        this.element_.classList.remove(this.CssClasses_.IS_FOCUSED)
    }, c.prototype.onMouseup_ = function(e) {
        this.blur_()
    }, c.prototype.updateClasses_ = function() {
        this.checkDisabled(), this.checkToggleState()
    }, c.prototype.blur_ = function() {
        window.setTimeout(function() {
            this.btnElement_.blur()
        }.bind(this), this.Constant_.TINY_TIMEOUT)
    }, c.prototype.checkDisabled = function() {
        this.btnElement_.disabled ? this.element_.classList.add(this.CssClasses_.IS_DISABLED) : this.element_.classList.remove(this.CssClasses_.IS_DISABLED)
    }, c.prototype.checkDisabled = c.prototype.checkDisabled, c.prototype.checkToggleState = function() {
        this.btnElement_.checked ? this.element_.classList.add(this.CssClasses_.IS_CHECKED) : this.element_.classList.remove(this.CssClasses_.IS_CHECKED)
    }, c.prototype.checkToggleState = c.prototype.checkToggleState, c.prototype.disable = function() {
        this.btnElement_.disabled = !0, this.updateClasses_()
    }, c.prototype.disable = c.prototype.disable, c.prototype.enable = function() {
        this.btnElement_.disabled = !1, this.updateClasses_()
    }, c.prototype.enable = c.prototype.enable, c.prototype.check = function() {
        this.btnElement_.checked = !0, this.onChange_(null)
    }, c.prototype.check = c.prototype.check, c.prototype.uncheck = function() {
        this.btnElement_.checked = !1, this.onChange_(null)
    }, c.prototype.uncheck = c.prototype.uncheck, c.prototype.init = function() {
        if (this.element_) {
            this.btnElement_ = this.element_.querySelector("." + this.CssClasses_.RADIO_BTN), this.boundChangeHandler_ = this.onChange_.bind(this), this.boundFocusHandler_ = this.onChange_.bind(this), this.boundBlurHandler_ = this.onBlur_.bind(this), this.boundMouseUpHandler_ = this.onMouseup_.bind(this);
            var e = document.createElement("span");
            e.classList.add(this.CssClasses_.RADIO_OUTER_CIRCLE);
            var t = document.createElement("span");
            t.classList.add(this.CssClasses_.RADIO_INNER_CIRCLE), this.element_.appendChild(e), this.element_.appendChild(t);
            var s;
            if (this.element_.classList.contains(this.CssClasses_.RIPPLE_EFFECT)) {
                this.element_.classList.add(this.CssClasses_.RIPPLE_IGNORE_EVENTS), s = document.createElement("span"), s.classList.add(this.CssClasses_.RIPPLE_CONTAINER), s.classList.add(this.CssClasses_.RIPPLE_EFFECT), s.classList.add(this.CssClasses_.RIPPLE_CENTER), s.addEventListener("mouseup", this.boundMouseUpHandler_);
                var i = document.createElement("span");
                i.classList.add(this.CssClasses_.RIPPLE), s.appendChild(i), this.element_.appendChild(s)
            }
            this.btnElement_.addEventListener("change", this.boundChangeHandler_), this.btnElement_.addEventListener("focus", this.boundFocusHandler_), this.btnElement_.addEventListener("blur", this.boundBlurHandler_), this.element_.addEventListener("mouseup", this.boundMouseUpHandler_), this.updateClasses_(), this.element_.classList.add(this.CssClasses_.IS_UPGRADED)
        }
    }, s.register({
        constructor: c,
        classAsString: "MaterialRadio",
        cssClass: "mdl-js-radio",
        widget: !0
    });
    var p = function(e) {
        this.element_ = e, this.isIE_ = window.navigator.msPointerEnabled, this.init()
    };
    window.MaterialSlider = p, p.prototype.Constant_ = {}, p.prototype.CssClasses_ = {
        IE_CONTAINER: "mdl-slider__ie-container",
        SLIDER_CONTAINER: "mdl-slider__container",
        BACKGROUND_FLEX: "mdl-slider__background-flex",
        BACKGROUND_LOWER: "mdl-slider__background-lower",
        BACKGROUND_UPPER: "mdl-slider__background-upper",
        IS_LOWEST_VALUE: "is-lowest-value",
        IS_UPGRADED: "is-upgraded"
    }, p.prototype.onInput_ = function(e) {
        this.updateValueStyles_()
    }, p.prototype.onChange_ = function(e) {
        this.updateValueStyles_()
    }, p.prototype.onMouseUp_ = function(e) {
        e.target.blur()
    }, p.prototype.onContainerMouseDown_ = function(e) {
        if (e.target === this.element_.parentElement) {
            e.preventDefault();
            var t = new MouseEvent("mousedown", {
                target: e.target,
                buttons: e.buttons,
                clientX: e.clientX,
                clientY: this.element_.getBoundingClientRect().y
            });
            this.element_.dispatchEvent(t)
        }
    }, p.prototype.updateValueStyles_ = function() {
        var e = (this.element_.value - this.element_.min) / (this.element_.max - this.element_.min);
        0 === e ? this.element_.classList.add(this.CssClasses_.IS_LOWEST_VALUE) : this.element_.classList.remove(this.CssClasses_.IS_LOWEST_VALUE), this.isIE_ || (this.backgroundLower_.style.flex = e, this.backgroundLower_.style.webkitFlex = e, this.backgroundUpper_.style.flex = 1 - e, this.backgroundUpper_.style.webkitFlex = 1 - e)
    }, p.prototype.disable = function() {
        this.element_.disabled = !0
    }, p.prototype.disable = p.prototype.disable, p.prototype.enable = function() {
        this.element_.disabled = !1
    }, p.prototype.enable = p.prototype.enable, p.prototype.change = function(e) {
        "undefined" != typeof e && (this.element_.value = e), this.updateValueStyles_()
    }, p.prototype.change = p.prototype.change, p.prototype.init = function() {
        if (this.element_) {
            if (this.isIE_) {
                var e = document.createElement("div");
                e.classList.add(this.CssClasses_.IE_CONTAINER), this.element_.parentElement.insertBefore(e, this.element_), this.element_.parentElement.removeChild(this.element_), e.appendChild(this.element_)
            } else {
                var t = document.createElement("div");
                t.classList.add(this.CssClasses_.SLIDER_CONTAINER), this.element_.parentElement.insertBefore(t, this.element_), this.element_.parentElement.removeChild(this.element_), t.appendChild(this.element_);
                var s = document.createElement("div");
                s.classList.add(this.CssClasses_.BACKGROUND_FLEX), t.appendChild(s), this.backgroundLower_ = document.createElement("div"), this.backgroundLower_.classList.add(this.CssClasses_.BACKGROUND_LOWER), s.appendChild(this.backgroundLower_), this.backgroundUpper_ = document.createElement("div"), this.backgroundUpper_.classList.add(this.CssClasses_.BACKGROUND_UPPER), s.appendChild(this.backgroundUpper_)
            }
            this.boundInputHandler = this.onInput_.bind(this), this.boundChangeHandler = this.onChange_.bind(this), this.boundMouseUpHandler = this.onMouseUp_.bind(this), this.boundContainerMouseDownHandler = this.onContainerMouseDown_.bind(this), this.element_.addEventListener("input", this.boundInputHandler), this.element_.addEventListener("change", this.boundChangeHandler), this.element_.addEventListener("mouseup", this.boundMouseUpHandler), this.element_.parentElement.addEventListener("mousedown", this.boundContainerMouseDownHandler), this.updateValueStyles_(), this.element_.classList.add(this.CssClasses_.IS_UPGRADED)
        }
    }, s.register({
        constructor: p,
        classAsString: "MaterialSlider",
        cssClass: "mdl-js-slider",
        widget: !0
    });
    var C = function(e) {
        if (this.element_ = e, this.textElement_ = this.element_.querySelector("." + this.cssClasses_.MESSAGE), this.actionElement_ = this.element_.querySelector("." + this.cssClasses_.ACTION), !this.textElement_) throw new Error("There must be a message element for a snackbar.");
        if (!this.actionElement_) throw new Error("There must be an action element for a snackbar.");
        this.active = !1, this.actionHandler_ = void 0, this.message_ = void 0, this.actionText_ = void 0, this.queuedNotifications_ = [], this.setActionHidden_(!0)
    };
    window.MaterialSnackbar = C, C.prototype.Constant_ = {
            ANIMATION_LENGTH: 250
        }, C.prototype.cssClasses_ = {
            SNACKBAR: "mdl-snackbar",
            MESSAGE: "mdl-snackbar__text",
            ACTION: "mdl-snackbar__action",
            ACTIVE: "mdl-snackbar--active"
        }, C.prototype.displaySnackbar_ = function() {
            this.element_.setAttribute("aria-hidden", "true"),