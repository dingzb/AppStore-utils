/**
 * Created by Neo on 2017/2/4.
 */

var layout = layout || {};

(function ($, layout) {
    layout.functions = {
        AGENCY: {
            id: 'menu_agency',
            title: '机构管理',
            icon: 'icon-tip',
            href: 'agency.html'
        },
        APP: {
            id: 'menu_app',
            title: '应用管理',
            icon: 'icon-sum',
            href: 'app.html'
        },
        FILTER: {
            id: 'menu_filter',
            title: '过滤器管理',
            icon: 'icon-sum',
            href: 'filter.html'
        },
        COLLECT: {
            id: 'menu_collect',
            title: '应用采集',
            icon: 'icon-save',
            href: 'collect.html'
        },
        VERSION: {
            id: 'menu_version',
            title: '版本管理',
            icon: 'icon-more',
            href: 'version.html'
        }
    };

    layout.openTab = function (tab, callback) {
        var tabs = $('#layout_center_tabs');
        var open = tabs.tabs('getTab', tab.title);
        if (open) {
            // open.panel('refresh', href);
            tabs.tabs('select', tab.title);
            if (typeof callback === 'function'){
                callback();
            }
        } else {
            tabs.tabs('add', {
                title: tab.title,
                iconCls: tab.icon,
                href: tab.href,
                closable: true,
                onLoad: callback
            });
        }
    };

    layout.init = function () {

        $.each(layout.functions, function (key, val) {
            $('#' + val.id).linkbutton({
                iconCls: val.icon,
                text: val.title,
                plain: true,
                onClick: function () {
                    layout.openTab(val);
                }
            });
        });

        ///////////////////
        $.extend($.fn.validatebox.defaults.rules, {
            unequals: {
                validator: function (value, param) {
                    return !(value === param[0]);
                },
                message: '该选项为必选项'
            }
        });
    }
})(jQuery, layout);
$(layout.init);