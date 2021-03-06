! function(t, e, i) {
    function o(i, o, n) {
        var r = e.createElement(i);
        return o && (r.id = Z + o), n && (r.style.cssText = n), t(r)
    }

    function n() {
        return i.innerHeight ? i.innerHeight : t(i).height()
    }

    function r(e, i) {
        i !== Object(i) && (i = {}), this.cache = {}, this.el = e, this.value = function(e) {
            var o;
            return void 0 === this.cache[e] && (o = t(this.el).attr("data-cbox-" + e), void 0 !== o ? this.cache[e] = o : void 0 !== i[e] ? this.cache[e] = i[e] : void 0 !== X[e] && (this.cache[e] = X[e])), this.cache[e]
        }, this.get = function(e) {
            var i = this.value(e);
            return t.isFunction(i) ? i.call(this.el, this) : i
        }
    }

    function h(t) {
        var e = E.length,
            i = (z + t) % e;
        return 0 > i ? e + i : i
    }

    function s(t, e) {
        return Math.round((/%/.test(t) ? ("x" === e ? W.width() : n()) / 100 : 1) * parseInt(t, 10))
    }

    function a(t, e) {
        return t.get("photo") || t.get("photoRegex").test(e)
    }

    function l(t, e) {
        return t.get("retinaUrl") && i.devicePixelRatio > 1 ? e.replace(t.get("photoRegex"), t.get("retinaSuffix")) : e
    }

    function d(t) {
        "contains" in x[0] && !x[0].contains(t.target) && t.target !== v[0] && (t.stopPropagation(), x.focus())
    }

    function c(t) {
        c.str !== t && (x.add(v).removeClass(c.str).addClass(t), c.str = t)
    }

    function g() {
        z = 0, rel && "nofollow" !== rel ? (E = t("." + te).filter(function() {
            var e = t.data(this, Y),
                i = new r(this, e);
            return i.get("rel") === rel
        }), z = E.index(_.el), -1 === z && (E = E.add(_.el), z = E.length - 1)) : E = t(_.el)
    }

    function u(i) {
        t(e).trigger(i), se.triggerHandler(i)
    }

    function f(i) {
        var n;
        G || (n = t(i).data("colorbox"), _ = new r(i, n), rel = _.get("rel"), g(), $ || ($ = q = !0, c(_.get("className")), x.css({
            visibility: "hidden",
            display: "block"
        }), L = o(ae, "LoadedContent", "width:0; height:0; overflow:hidden; visibility:hidden"), b.css({
            width: "",
            height: ""
        }).append(L), D = T.height() + k.height() + b.outerHeight(!0) - b.height(), j = C.width() + H.width() + b.outerWidth(!0) - b.width(), A = L.outerHeight(!0), N = L.outerWidth(!0), _.w = s(_.get("initialWidth"), "x"), _.h = s(_.get("initialHeight"), "y"), L.css({
            width: "",
            height: _.h
        }), J.position(), u(ee), _.get("onOpen"), O.add(R).hide(), x.focus(), _.get("trapFocus") && e.addEventListener && (e.addEventListener("focus", d, !0), se.one(re, function() {
            e.removeEventListener("focus", d, !0)
        })), _.get("returnFocus") && se.one(re, function() {
            t(_.el).focus()
        })), v.css({
            opacity: parseFloat(_.get("opacity")),
            cursor: _.get("overlayClose") ? "pointer" : "auto",
            visibility: "visible"
        }).show(), _.get("closeButton") ? B.html(_.get("close")).appendTo(b) : B.appendTo("<div/>"), w())
    }

    function p() {
        !x && e.body && (V = !1, W = t(i), x = o(ae).attr({
            id: Y,
            "class": t.support.opacity === !1 ? Z + "IE" : "",
            role: "dialog",
            tabindex: "-1"
        }).hide(), v = o(ae, "Overlay").hide(), M = t([o(ae, "LoadingOverlay")[0], o(ae, "LoadingGraphic")[0]]), y = o(ae, "Wrapper"), b = o(ae, "Content").append(R = o(ae, "Title"), F = o(ae, "Current"), P = t('<button type="button"/>').attr({
            id: Z + "Previous"
        }), K = t('<button type="button"/>').attr({
            id: Z + "Next"
        }), I = o("button", "Slideshow"), M), B = t('<button type="button"/>').attr({
            id: Z + "Close"
        }), y.append(o(ae).append(o(ae, "TopLeft"), T = o(ae, "TopCenter"), o(ae, "TopRight")), o(ae, !1, "clear:left").append(C = o(ae, "MiddleLeft"), b, H = o(ae, "MiddleRight")), o(ae, !1, "clear:left").append(o(ae, "BottomLeft"), k = o(ae, "BottomCenter"), o(ae, "BottomRight"))).find("div div").css({
            "float": "left"
        }), S = o(ae, !1, "position:absolute; width:9999px; visibility:hidden; display:none; max-width:none;"), O = K.add(P).add(F).add(I), t(e.body).append(v, x.append(y, S)))
    }

    function m() {
        function i(t) {
            t.which > 1 || t.shiftKey || t.altKey || t.metaKey || t.ctrlKey || (t.preventDefault(), f(this))
        }
        return x ? (V || (V = !0, K.click(function() {
            J.next()
        }), P.click(function() {
            J.prev()
        }), B.click(function() {
            J.close()
        }), v.click(function() {
            _.get("overlayClose") && J.close()
        }), t(e).bind("keydown." + Z, function(t) {
            var e = t.keyCode;
            $ && _.get("escKey") && 27 === e && (t.preventDefault(), J.close()), $ && _.get("arrowKey") && E[1] && !t.altKey && (37 === e ? (t.preventDefault(), P.click()) : 39 === e && (t.preventDefault(), K.click()))
        }), t.isFunction(t.fn.on) ? t(e).on("click." + Z, "." + te, i) : t("." + te).live("click." + Z, i)), !0) : !1
    }

    function w() {
        var n, r, h, d = J.prep,
            c = ++le;
        q = !0, U = !1, u(he), u(ie), _.get("onLoad"), _.h = _.get("height") ? s(_.get("height"), "y") - A - D : _.get("innerHeight") && s(_.get("innerHeight"), "y"), _.w = _.get("width") ? s(_.get("width"), "x") - N - j : _.get("innerWidth") && s(_.get("innerWidth"), "x"), _.mw = _.w, _.mh = _.h, _.get("maxWidth") && (_.mw = s(_.get("maxWidth"), "x") - N - j, _.mw = _.w && _.w < _.mw ? _.w : _.mw), _.get("maxHeight") && (_.mh = s(_.get("maxHeight"), "y") - A - D, _.mh = _.h && _.h < _.mh ? _.h : _.mh), n = _.get("href"), Q = setTimeout(function() {
            M.show()
        }, 100), _.get("inline") ? (h = o(ae).hide().insertBefore(t(n)[0]), se.one(he, function() {
            h.replaceWith(L.children())
        }), d(t(n))) : _.get("iframe") ? d(" ") : _.get("html") ? d(_.get("html")) : a(_, n) ? (n = l(_, n), U = e.createElement("img"), t(U).addClass(Z + "Photo").bind("error", function() {
            d(o(ae, "Error").html(_.get("imgError")))
        }).one("load", function() {
            var e;
            c === le && (t.each(["alt", "longdesc", "aria-describedby"], function(e, i) {
                var o = t(_.el).attr(i) || t(_.el).attr("data-" + i);
                o && U.setAttribute(i, o)
            }), _.get("retinaImage") && i.devicePixelRatio > 1 && (U.height = U.height / i.devicePixelRatio, U.width = U.width / i.devicePixelRatio), _.get("scalePhotos") && (r = function() {
                U.height -= U.height * e, U.width -= U.width * e
            }, _.mw && U.width > _.mw && (e = (U.width - _.mw) / U.width, r()), _.mh && U.height > _.mh && (e = (U.height - _.mh) / U.height, r())), _.h && (U.style.marginTop = Math.max(_.mh - U.height, 0) / 2 + "px"), E[1] && (_.get("loop") || E[z + 1]) && (U.style.cursor = "pointer", U.onclick = function() {
                J.next()
            }), U.style.width = U.width + "px", U.style.height = U.height + "px", setTimeout(function() {
                d(U)
            }, 1))
        }), setTimeout(function() {
            U.src = n
        }, 1)) : n && S.load(n, _.get("data"), function(e, i) {
            c === le && d("error" === i ? o(ae, "Error").html(_.get("xhrError")) : t(this).contents())
        })
    }
    var v, x, y, b, T, C, H, k, E, W, L, S, M, R, F, I, K, P, B, O, _, D, j, A, N, z, U, $, q, G, Q, J, V, X = {
            html: !1,
            photo: !1,
            iframe: !1,
            inline: !1,
            transition: "elastic",
            speed: 300,
            fadeOut: 300,
            width: !1,
            initialWidth: "600",
            innerWidth: !1,
            maxWidth: !1,
            height: !1,
            initialHeight: "450",
            innerHeight: !1,
            maxHeight: !1,
            scalePhotos: !0,
            scrolling: !0,
            opacity: .9,
            preloading: !0,
            className: !1,
            overlayClose: !0,
            escKey: !0,
            arrowKey: !0,
            top: !1,
            bottom: !1,
            left: !1,
            right: !1,
            fixed: !1,
            data: void 0,
            closeButton: !0,
            fastIframe: !0,
            open: !1,
            reposition: !0,
            loop: !0,
            slideshow: !1,
            slideshowAuto: !0,
            slideshowSpeed: 2500,
            slideshowStart: "start slideshow",
            slideshowStop: "stop slideshow",
            photoRegex: /\.(gif|png|jp(e|g|eg)|bmp|ico|webp|jxr|svg)((#|\?).*)?$/i,
            retinaImage: !1,
            retinaUrl: !1,
            retinaSuffix: "@2x.$1",
            current: "image {current} of {total}",
            previous: "previous",
            next: "next",
            close: "close",
            xhrError: "This content failed to load.",
            imgError: "This image failed to load.",
            returnFocus: !0,
            trapFocus: !0,
            onOpen: !1,
            onLoad: !1,
            onComplete: !1,
            onCleanup: !1,
            onClosed: !1,
            rel: function() {
                return this.rel
            },
            href: function() {
                return t(this).attr("href")
            },
            title: function() {
                return this.title
            }
        },
        Y = "colorbox",
        Z = "cbox",
        te = Z + "Element",
        ee = Z + "_open",
        ie = Z + "_load",
        oe = Z + "_complete",
        ne = Z + "_cleanup",
        re = Z + "_closed",
        he = Z + "_purge",
        se = t("<a/>"),
        ae = "div",
        le = 0,
        de = {},
        ce = function() {
            function t() {
                clearTimeout(h)
            }

            function e() {
                (_.get("loop") || E[z + 1]) && (t(), h = setTimeout(J.next, _.get("slideshowSpeed")))
            }

            function i() {
                I.html(_.get("slideshowStop")).unbind(a).one(a, o), se.bind(oe, e).bind(ie, t), x.removeClass(s + "off").addClass(s + "on")
            }

            function o() {
                t(), se.unbind(oe, e).unbind(ie, t), I.html(_.get("slideshowStart")).unbind(a).one(a, function() {
                    J.next(), i()
                }), x.removeClass(s + "on").addClass(s + "off")
            }

            function n() {
                r = !1, I.hide(), t(), se.unbind(oe, e).unbind(ie, t), x.removeClass(s + "off " + s + "on")
            }
            var r, h, s = Z + "Slideshow_",
                a = "click." + Z;
            return function() {
                r ? _.get("slideshow") || (se.unbind(ne, n), n()) : _.get("slideshow") && E[1] && (r = !0, se.one(ne, n), _.get("slideshowAuto") ? i() : o(), I.show())
            }
        }();
    t.colorbox || (t(p), J = t.fn[Y] = t[Y] = function(e, i) {
        var o, n = this;
        if (e = e || {}, t.isFunction(n)) n = t("<a/>"), e.open = !0;
        else if (!n[0]) return n;
        return n[0] ? (p(), m() && (i && (e.onComplete = i), n.each(function() {
            var i = t.data(this, Y) || {};
            t.data(this, Y, t.extend(i, e))
        }).addClass(te), o = new r(n[0], e), o.get("open") && f(n[0])), n) : n
    }, J.position = function(e, i) {
        function o() {
            T[0].style.width = k[0].style.width = b[0].style.width = parseInt(x[0].style.width, 10) - j + "px", b[0].style.height = C[0].style.height = H[0].style.height = parseInt(x[0].style.height, 10) - D + "px"
        }
        var r, h, a, l = 0,
            d = 0,
            c = x.offset();
        if (W.unbind("resize." + Z), x.css({
                top: -9e4,
                left: -9e4
            }), h = W.scrollTop(), a = W.scrollLeft(), _.get("fixed") ? (c.top -= h, c.left -= a, x.css({
                position: "fixed"
            })) : (l = h, d = a, x.css({
                position: "absolute"
            })), d += _.get("right") !== !1 ? Math.max(W.width() - _.w - N - j - s(_.get("right"), "x"), 0) : _.get("left") !== !1 ? s(_.get("left"), "x") : Math.round(Math.max(W.width() - _.w - N - j, 0) / 2), l += _.get("bottom") !== !1 ? Math.max(n() - _.h - A - D - s(_.get("bottom"), "y"), 0) : _.get("top") !== !1 ? s(_.get("top"), "y") : Math.round(Math.max(n() - _.h - A - D, 0) / 2), x.css({
                top: c.top,
                left: c.left,
                visibility: "visible"
            }), y[0].style.width = y[0].style.height = "9999px", r = {
                width: _.w + N + j,
                height: _.h + A + D,
                top: l,
                left: d
            }, e) {
            var g = 0;
            t.each(r, function(t) {
                return r[t] !== de[t] ? void(g = e) : void 0
            }), e = g
        }
        de = r, e || x.css(r), x.dequeue().animate(r, {
            duration: e || 0,
            complete: function() {
                o(), q = !1, y[0].style.width = _.w + N + j + "px", y[0].style.height = _.h + A + D + "px", _.get("reposition") && setTimeout(function() {
                    W.bind("resize." + Z, J.position)
                }, 1), i && i()
            },
            step: o
        })
    }, J.resize = function(t) {
        var e;
        $ && (t = t || {}, t.width && (_.w = s(t.width, "x") - N - j), t.innerWidth && (_.w = s(t.innerWidth, "x")), L.css({
            width: _.w
        }), t.height && (_.h = s(t.height, "y") - A - D), t.innerHeight && (_.h = s(t.innerHeight, "y")), t.innerHeight || t.height || (e = L.scrollTop(), L.css({
            height: "auto"
        }), _.h = L.height()), L.css({
            height: _.h
        }), e && L.scrollTop(e), J.position("none" === _.get("transition") ? 0 : _.get("speed")))
    }, J.prep = function(i) {
        function n() {
            return _.w = _.w || L.width(), _.w = _.mw && _.mw < _.w ? _.mw : _.w, _.w
        }

        function s() {
            return _.h = _.h || L.height(), _.h = _.mh && _.mh < _.h ? _.mh : _.h, _.h
        }
        if ($) {
            var d, g = "none" === _.get("transition") ? 0 : _.get("speed");
            L.remove(), L = o(ae, "LoadedContent").append(i), L.hide().appendTo(S.show()).css({
                width: n(),
                overflow: _.get("scrolling") ? "auto" : "hidden"
            }).css({
                height: s()
            }).prependTo(b), S.hide(), t(U).css({
                "float": "none"
            }), c(_.get("className")), d = function() {
                function i() {
                    t.support.opacity === !1 && x[0].style.removeAttribute("filter")
                }
                var o, n, s = E.length;
                $ && (n = function() {
                    clearTimeout(Q), M.hide(), u(oe), _.get("onComplete")
                }, R.html(_.get("title")).show(), L.show(), s > 1 ? ("string" == typeof _.get("current") && F.html(_.get("current").replace("{current}", z + 1).replace("{total}", s)).show(), K[_.get("loop") || s - 1 > z ? "show" : "hide"]().html(_.get("next")), P[_.get("loop") || z ? "show" : "hide"]().html(_.get("previous")), ce(), _.get("preloading") && t.each([h(-1), h(1)], function() {
                    var i, o = E[this],
                        n = new r(o, t.data(o, Y)),
                        h = n.get("href");
                    h && a(n, h) && (h = l(n, h), i = e.createElement("img"), i.src = h)
                })) : O.hide(), _.get("iframe") ? (o = e.createElement("iframe"), "frameBorder" in o && (o.frameBorder = 0), "allowTransparency" in o && (o.allowTransparency = "true"), _.get("scrolling") || (o.scrolling = "no"), t(o).attr({
                    src: _.get("href"),
                    name: (new Date).getTime(),
                    "class": Z + "Iframe",
                    allowFullScreen: !0
                }).one("load", n).appendTo(L), se.one(he, function() {
                    o.src = "//about:blank"
                }), _.get("fastIframe") && t(o).trigger("load")) : n(), "fade" === _.get("transition") ? x.fadeTo(g, 1, i) : i())
            }, "fade" === _.get("transition") ? x.fadeTo(g, 0, function() {
                J.position(0, d)
            }) : J.position(g, d)
        }
    }, J.next = function() {
        !q && E[1] && (_.get("loop") || E[z + 1]) && (z = h(1), f(E[z]))
    }, J.prev = function() {
        !q && E[1] && (_.get("loop") || z) && (z = h(-1), f(E[z]))
    }, J.close = function() {
        $ && !G && (G = !0, $ = !1, u(ne), _.get("onCleanup"), W.unbind("." + Z), v.fadeTo(_.get("fadeOut") || 0, 0), x.stop().fadeTo(_.get("fadeOut") || 0, 0, function() {
            x.add(v).css({
                opacity: 1,
                cursor: "auto"
            }).hide(), u(he), L.remove(), setTimeout(function() {
                G = !1, u(re), _.get("onClosed")
            }, 1)
        }))
    }, J.remove = function() {
        x && (x.stop(), t.colorbox.close(), x.stop().remove(), v.remove(), G = !1, x = null, t("." + te).removeData(Y).removeClass(te), t(e).unbind("click." + Z))
    }, J.element = function() {
        return t(_.el)
    }, J.settings = X)
}(jQuery, document, window);
jQuery.fn.selectbox = function(e) {
    var s = {
            className: "jquery-selectbox",
            animationSpeed: "normal",
            listboxMaxSize: 30,
            replaceInvisible: !1
        },
        t = "jquery-custom-selectboxes-replaced",
        a = !1,
        i = function(e) {
            var t = e.parents("." + s.className);
            return e.slideDown(s.animationSpeed, function() {
                a = !0
            }), t.addClass("selecthover"), jQuery(document).bind("click", r), e
        },
        n = function(e) {
            e.parents("." + s.className);
            return e.slideUp(s.animationSpeed, function() {
                a = !1, jQuery(this).parents("." + s.className).removeClass("selecthover")
            }), jQuery(document).unbind("click", r), e
        },
        r = function(e) {
            var i = e.target,
                r = jQuery("." + s.className + "-list:visible").parent().find("*").addBack();
            return jQuery.inArray(i, r) < 0 && a && n(jQuery("." + t + "-list")), !1
        };
    return s = jQuery.extend(s, e || {}), this.each(function() {
        var e = jQuery(this);
        if (0 != e.filter(":visible").length || s.replaceInvisible) {
            var a = jQuery('<div class="' + s.className + " " + t + '"><div class="' + s.className + '-moreButton" /><div class="' + s.className + "-list " + t + '-list" /><span class="' + s.className + '-currentItem" /></div>');
            jQuery("option", e).each(function(e, t) {
                var t = jQuery(t),
                    r = jQuery('<span class="' + s.className + "-item value-" + t.val() + " item-" + e + '">' + t.text() + "</span>");
                r.click(function() {
                    var e = jQuery(this),
                        t = e.parents("." + s.className),
                        a = e[0].className.split(" ");
                    for (k1 in a)
                        if (/^item-[0-9]+$/.test(a[k1])) {
                            a = parseInt(a[k1].replace("item-", ""), 10);
                            break
                        }
                    var r = e[0].className.split(" ");
                    for (k1 in r)
                        if (/^value-.+$/.test(r[k1])) {
                            r = r[k1].replace("value-", "");
                            break
                        }
                    t.find("." + s.className + "-currentItem").text(e.text()), t.find("select").val(r).triggerHandler("change");
                    var l = t.find("." + s.className + "-list");
                    l.filter(":visible").length > 0 ? n(l) : i(l)
                }).bind("mouseenter", function() {
                    jQuery(this).addClass("listelementhover")
                }).bind("mouseleave", function() {
                    jQuery(this).removeClass("listelementhover")
                }), jQuery("." + s.className + "-list", a).append(r), t.filter(":selected").length > 0 && jQuery("." + s.className + "-currentItem", a).text(t.text())
            }), a.find("." + s.className + "-moreButton").click(function() {
                var e = jQuery(this),
                    t = jQuery("." + s.className + "-list").not(e.siblings("." + s.className + "-list"));
                n(t);
                var a = e.siblings("." + s.className + "-list");
                a.filter(":visible").length > 0 ? n(a) : i(a)
            }).bind("mouseenter", function() {
                jQuery(this).addClass("morebuttonhover")
            }).bind("mouseleave", function() {
                jQuery(this).removeClass("morebuttonhover")
            }), e.hide().replaceWith(a).appendTo(a);
            var r = a.find("." + s.className + "-list"),
                l = r.find("." + s.className + "-item").length;
            l > s.listboxMaxSize && (l = s.listboxMaxSize), 0 == l && (l = 1);
            var c = Math.round(e.width() + 5); - 1 != navigator.userAgent.indexOf("Safari") && -1 == navigator.userAgent.indexOf("Chrome") && (c = .98 * c), a.css("width", "175px"), r.css({
                width: Math.round(c - 5) + "px",
                height: s.listboxMaxSize + "px"
            })
        }
    })
}, jQuery.fn.unselectbox = function() {
    var e = "jquery-custom-selectboxes-replaced";
    return this.each(function() {
        var s = jQuery(this).filter("." + e);
        s.replaceWith(s.find("select").show())
    })
};
! function(s) {
    var e = function() {
        var e = {
                bcClass: "sf-breadcrumb",
                menuClass: "sf-js-enabled",
                anchorClass: "sf-with-ul",
                menuArrowClass: "sf-arrows"
            },
            n = /iPhone|iPad|iPod/i.test(navigator.userAgent),
            t = function() {
                var s = document.documentElement.style;
                return "behavior" in s && "fill" in s && /iemobile/i.test(navigator.userAgent)
            }(),
            o = (function() {
                n && s(window).on('load', function() {
                    s("body").children().on("click", s.noop)
                })
            }(), function(s, n) {
                var t = e.menuClass;
                n.cssArrows && (t += " " + e.menuArrowClass), s.toggleClass(t)
            }),
            i = function(n, t) {
                return n.find("li." + t.pathClass).slice(0, t.pathLevels).addClass(t.hoverClass + " " + e.bcClass).filter(function() {
                    return s(this).children("ul").hide().show().length
                }).removeClass(t.pathClass)
            },
            r = function(s) {
                s.children("a").toggleClass(e.anchorClass)
            },
            a = function(s) {
                var e = s.css("ms-touch-action");
                e = "pan-y" === e ? "auto" : "pan-y", s.css("ms-touch-action", e)
            },
            h = function(e, o) {
                var i = "li:has(ul)";
                s.fn.hoverIntent && !o.disableHI ? e.hoverIntent(u, f, i) : e.on("mouseenter.superfish", i, u).on("mouseleave.superfish", i, f);
                var r = "MSPointerDown.superfish";
                n || (r += " touchend.superfish"), t && (r += " mousedown.superfish"), e.on("focusin.superfish", "li", u).on("focusout.superfish", "li", f).on(r, "a", l)
            },
            l = function(e) {
                var n = s(this),
                    t = n.siblings("ul");
                t.length > 0 && t.is(":hidden") && (n.one("click.superfish", !1), "MSPointerDown" === e.type ? n.trigger("focus") : s.proxy(u, n.parent("li"))())
            },
            u = function() {
                var e = s(this),
                    n = d(e);
                clearTimeout(n.sfTimer), e.siblings().superfish("hide").end().superfish("show")
            },
            f = function() {
                var e = s(this),
                    t = d(e);
                n ? s.proxy(c, e, t)() : (clearTimeout(t.sfTimer), t.sfTimer = setTimeout(s.proxy(c, e, t), t.delay))
            },
            c = function(e) {
                e.retainPath = s.inArray(this[0], e.$path) > -1, this.superfish("hide"), this.parents("." + e.hoverClass).length || (e.onIdle.call(p(this)), e.$path.length && s.proxy(u, e.$path)())
            },
            p = function(s) {
                return s.closest("." + e.menuClass)
            },
            d = function(s) {
                return p(s).data("sf-options")
            };
        return {
            hide: function(e) {
                if (this.length) {
                    var n = this,
                        t = d(n);
                    if (!t) return this;
                    var o = t.retainPath === !0 ? t.$path : "",
                        i = n.find("li." + t.hoverClass).add(this).not(o).removeClass(t.hoverClass).children("ul"),
                        r = t.speedOut;
                    e && (i.show(), r = 0), t.retainPath = !1, t.onBeforeHide.call(i), i.stop(!0, !0).animate(t.animationOut, r, function() {
                        var e = s(this);
                        t.onHide.call(e)
                    })
                }
                return this
            },
            show: function() {
                var s = d(this);
                if (!s) return this;
                var e = this.addClass(s.hoverClass),
                    n = e.children("ul");
                return s.onBeforeShow.call(n), n.stop(!0, !0).animate(s.animation, s.speed, function() {
                    s.onShow.call(n)
                }), this
            },
            destroy: function() {
                return this.each(function() {
                    var n = s(this),
                        t = n.data("sf-options"),
                        i = n.find("li:has(ul)");
                    return t ? (clearTimeout(t.sfTimer), o(n, t), r(i), a(n), n.off(".superfish").off(".hoverIntent"), i.children("ul").attr("style", function(s, e) {
                        return e.replace(/display[^;]+;?/g, "")
                    }), t.$path.removeClass(t.hoverClass + " " + e.bcClass).addClass(t.pathClass), n.find("." + t.hoverClass).removeClass(t.hoverClass), t.onDestroy.call(n), void n.removeData("sf-options")) : !1
                })
            },
            init: function(n) {
                return this.each(function() {
                    var t = s(this);
                    if (t.data("sf-options")) return !1;
                    var l = s.extend({}, s.fn.superfish.defaults, n),
                        u = t.find("li:has(ul)");
                    l.$path = i(t, l), t.data("sf-options", l), o(t, l), r(u), a(t), h(t, l), u.not("." + e.bcClass).superfish("hide", !0), l.onInit.call(this)
                })
            }
        }
    }();
    s.fn.superfish = function(n) {
        return e[n] ? e[n].apply(this, Array.prototype.slice.call(arguments, 1)) : "object" != typeof n && n ? s.error("Method " + n + " does not exist on jQuery.fn.superfish") : e.init.apply(this, arguments)
    }, s.fn.superfish.defaults = {
        hoverClass: "sfHover",
        pathClass: "overrideThisToUse",
        pathLevels: 1,
        delay: 800,
        animation: {
            opacity: "show"
        },
        animationOut: {
            opacity: "hide"
        },
        speed: "normal",
        speedOut: "fast",
        cssArrows: !0,
        disableHI: !1,
        onInit: s.noop,
        onBeforeShow: s.noop,
        onShow: s.noop,
        onBeforeHide: s.noop,
        onHide: s.noop,
        onIdle: s.noop,
        onDestroy: s.noop
    }, s.fn.extend({
        hideSuperfishUl: e.hide,
        showSuperfishUl: e.show
    })
}(jQuery);
! function(e) {
    e.fn.jflickrfeed = function(i, a) {
        i = e.extend(!0, {
            flickrbase: "//api.flickr.com/services/feeds/",
            feedapi: "photos_public.gne",
            limit: 20,
            qstrings: {
                lang: "en-us",
                format: "json",
                jsoncallback: "?"
            },
            cleanDescription: !0,
            useTemplate: !0,
            itemTemplate: "",
            itemCallback: function() {}
        }, i);
        var t = i.flickrbase + i.feedapi + "?",
            c = !0;
        for (var n in i.qstrings) c || (t += "&"), t += n + "=" + i.qstrings[n], c = !1;
        return e(this).each(function() {
            var c = e(this),
                n = this;
            e.getJSON(t, function(t) {
                e.each(t.items, function(e, a) {
                    if (e < i.limit) {
                        if (i.cleanDescription) {
                            var t = /<p>(.*?)<\/p>/g,
                                m = a.description;
                            t.test(m) && (a.description = m.match(t)[2], void 0 != a.description && (a.description = a.description.replace("<p>", "").replace("</p>", "")))
                        }
                        if (a.image_s = a.media.m.replace("_m", "_s"), a.image_t = a.media.m.replace("_m", "_t"), a.image_m = a.media.m.replace("_m", "_m"), a.image = a.media.m.replace("_m", ""), a.image_b = a.media.m.replace("_m", "_b"), delete a.media, i.useTemplate) {
                            var r = i.itemTemplate;
                            for (var l in a) {
                                var s = new RegExp("{{" + l + "}}", "g");
                                r = r.replace(s, a[l])
                            }
                            c.append(r)
                        }
                        i.itemCallback.call(n, a)
                    }
                }), e.isFunction(a) && a.call(n, t)
            })
        })
    }
}(jQuery);
(function(a) {
    function f(a) {
        document.location.href = a
    }

    function g() {
        return a(".mnav").length ? !0 : !1
    }

    function h(b) {
        var c = !0;
        b.each(function() {
            if (!a(this).is("ul") && !a(this).is("ol")) {
                c = !1;
            }
        });
        return c
    }

    function i() {
        return a(window).width() < b.switchWidth
    }

    function j(b) {
        return a.trim(b.clone().children("ul, ol").remove().end().text())
    }

    function k(b) {
        return a.inArray(b, e) === -1 ? !0 : !1
    }

    function l(b) {
        b.find(" > li").each(function() {
            var c = a(this),
                d = c.find("a").attr("href"),
                f = function() {
                    return c.parent().parent().is("li") ? c.parent().parent().find("a").attr("href") : null
                };
            c.find(" ul, ol").length && l(c.find("> ul, > ol"));
            c.find(" > ul li, > ol li").length || c.find("ul, ol").remove();
            !k(f(), e) && k(d, e) ? c.appendTo(b.closest("ul#mmnav").find("li:has(a[href=" + f() + "]):first ul")) : k(d) ? e.push(d) : c.remove()
        })
    }

    function m() {
        var b = a('<ul id="mmnav" />');
        c.each(function() {
            a(this).children().clone().appendTo(b)
        });
        l(b);
        return b
    }

    function n(b, c, d) {
        d ? a('<option value="' + b.find("a:first").attr("href") + '">' + d + "</option>").appendTo(c) : a('<option value="' + b.find("a:first").attr("href") + '">' + a.trim(j(b)) + "</option>").appendTo(c)
    }

    function o(c, d) {
        var e = a('<optgroup label="' + a.trim(j(c)) + '" />');
        n(c, e, b.groupPageText);
        c.children("ul, ol").each(function() {
            a(this).children("li").each(function() {
                n(a(this), e)
            })
        });
        e.appendTo(d)
    }

    function p(c) {
        var e = a('<select id="mm' + d + '" class="mnav" />');
        d++;
        b.topOptionText && n(a("<li>" + b.topOptionText + "</li>"), e);
        c.children("li").each(function() {
            var c = a(this);
            c.children("ul, ol").length && b.nested ? o(c, e) : n(c, e)
        });
        e.change(function() {
            f(a(this).val())
        }).prependTo(b.prependTo)
    }

    function q() {
        if (i() && !g())
            if (b.combine) {
                var d = m();
                p(d)
            } else c.each(function() {
                p(a(this))
            });
        if (i() && g()) {
            a(".mnav").show();
            c.hide()
        }
        if (!i() && g()) {
            a(".mnav").hide();
            c.show()
        }
    }
    var b = {
            combine: !0,
            groupPageText: "Main",
            nested: !0,
            prependTo: "body",
            switchWidth: 480,
            topOptionText: "Select a page"
        },
        c, d = 0,
        e = [];
    a.fn.mobileMenu = function(d) {
        d && a.extend(b, d);
        if (h(a(this))) {
            c = a(this);
            q();
            a(window).resize(function() {
                q()
            })
        } else alert("mobileMenu only works with <ul>/<ol>")
    }
})(jQuery);
/*! http://mths.be/placeholder v2.0.7 by @mathias */
;
(function(f, h, $) {
    var a = 'placeholder' in h.createElement('input'),
        d = 'placeholder' in h.createElement('textarea'),
        i = $.fn,
        c = $.valHooks,
        k, j;
    if (a && d) {
        j = i.placeholder = function() {
            return this
        };
        j.input = j.textarea = true
    } else {
        j = i.placeholder = function() {
            var l = this;
            l.filter((a ? 'textarea' : ':input') + '[placeholder]').not('.placeholder').bind({
                'focus.placeholder': b,
                'blur.placeholder': e
            }).data('placeholder-enabled', true).trigger('blur.placeholder');
            return l
        };
        j.input = a;
        j.textarea = d;
        k = {
            get: function(m) {
                var l = $(m);
                return l.data('placeholder-enabled') && l.hasClass('placeholder') ? '' : m.value
            },
            set: function(m, n) {
                var l = $(m);
                if (!l.data('placeholder-enabled')) {
                    return m.value = n
                }
                if (n == '') {
                    m.value = n;
                    if (m != h.activeElement) {
                        e.call(m)
                    }
                } else {
                    if (l.hasClass('placeholder')) {
                        b.call(m, true, n) || (m.value = n)
                    } else {
                        m.value = n
                    }
                }
                return l
            }
        };
        a || (c.input = k);
        d || (c.textarea = k);
        $(function() {
            $(h).delegate('form', 'submit.placeholder', function() {
                var l = $('.placeholder', this).each(b);
                setTimeout(function() {
                    l.each(e)
                }, 10)
            })
        });
        $(f).bind('beforeunload.placeholder', function() {
            $('.placeholder').each(function() {
                this.value = ''
            })
        })
    }

    function g(m) {
        var l = {},
            n = /^jQuery\d+$/;
        $.each(m.attributes, function(p, o) {
            if (o.specified && !n.test(o.name)) {
                l[o.name] = o.value
            }
        });
        return l
    }

    function b(m, n) {
        var l = this,
            o = $(l);
        if (l.value == o.attr('placeholder') && o.hasClass('placeholder')) {
            if (o.data('placeholder-password')) {
                o = o.hide().next().show().attr('id', o.removeAttr('id').data('placeholder-id'));
                if (m === true) {
                    return o[0].value = n
                }
                o.focus()
            } else {
                l.value = '';
                o.removeClass('placeholder');
                l == h.activeElement && l.select()
            }
        }
    }

    function e() {
        var q, l = this,
            p = $(l),
            m = p,
            o = this.id;
        if (l.value == '') {
            if (l.type == 'password') {
                if (!p.data('placeholder-textinput')) {
                    try {
                        q = p.clone().attr({
                            type: 'text'
                        })
                    } catch (n) {
                        q = $('<input>').attr($.extend(g(this), {
                            type: 'text'
                        }))
                    }
                    q.removeAttr('name').data({
                        'placeholder-password': true,
                        'placeholder-id': o
                    }).bind('focus.placeholder', b);
                    p.data({
                        'placeholder-textinput': q,
                        'placeholder-id': o
                    }).before(q)
                }
                p = p.removeAttr('id').hide().prev().attr('id', o).show()
            }
            p.addClass('placeholder');
            p[0].value = p.attr('placeholder')
        } else {
            p.removeClass('placeholder')
        }
    }
}(this, document, jQuery));
! function(e) {
    e.fn.equalHeights = function(e, i) {
        var t = 0;
        "string" == typeof document.body.style.MozBoxShadow && (t = window.innerWidth - jQuery("body").width());
        var s = jQuery(this).children().length;
        jQuery(this).children().css("min-height", "auto");
        for (var o = 1, n = 0; s >= o;) {
            var r = jQuery(this).children(":nth-child(" + o.toString() + ")").height();
            r >= n && (n = r), o += 1
        }
        jQuery("body").width() > e - t ? jQuery(this).children().css("min-height", n + i) : jQuery(this).children().css("min-height", "auto")
    }
}(jQuery), jQuery(document).ready(function() {
    jQuery("#sf-menu").superfish(), jQuery("#sf-menu").mobileMenu({
        switchWidth: 767,
        prependTo: ".main-menu",
        combine: !1
    }), $("#contourimage").on('error', function() {
        $(this).css({
            visibility: "hidden"
        }), $("#contourlink").css({
            visibility: "hidden"
        })
    }), $(".page-selector select").selectbox({
        animationSpeed: "fast",
        listboxMaxSize: 400
    }), $("#category-selector-default").selectbox({
        animationSpeed: "fast",
        listboxMaxSize: 400
    }), $("#category-selector-advanced").selectbox({
        animationSpeed: "fast",
        listboxMaxSize: 400
    }), $("#country-selector-advanced").selectbox({
        animationSpeed: "fast",
        listboxMaxSize: 400
    }), $(".language-selector select").selectbox({
        animationSpeed: "fast",
        listboxMaxSize: 400
    }), jQuery(".text-input-grey, .text-input-black").placeholder(), jQuery("#login-link").click(function() {
        jQuery("#login-form").toggle(), jQuery("#register-form").hide()
    }), jQuery("#register-link").click(function() {
        jQuery("#register-form").toggle(), jQuery("#login-form").hide()
    })
}), jQuery(window).on('load', function() {
    $("#flickr-feed").jflickrfeed({
        limit: 12,
        qstrings: {
            id: "37074442@N00"
        },
        itemTemplate: '<li><a class="group1" href="{{image_b}}"><img src="{{image_s}}" alt="{{title}}" /></a></li>'
    }, function() {
        $("#flickr-feed a").colorbox({
            rel: "group1",
            maxWidth: "90%"
        })
    }), jQuery(".equalize").equalHeights(767, 0), jQuery("#subscription-options").equalHeights(450, 1), $(".specialisation-progressbar").each(function() {
        var e = Number($(this).attr("value"));
        $(this).progressbar({
            value: e
        })
    }), $(".specialisation .toggle-description-button").click(function() {
        1 == $(this).hasClass("plus-button") ? $(this).toggleClass("plus-button minus-button").html("-").siblings(".specialisation-description").slideDown() : $(this).toggleClass("plus-button minus-button").html("+").siblings(".specialisation-description").slideUp()
    }), jQuery(".zone-content.equalize").equalHeights(767, 0), jQuery("#subscription-options").equalHeights(450, 1)
});
