/**
 * Created by Neo on 2017/2/4.
 */

var version = version || {};

(function ($, version) {

    var colors = [];
    var exist = [];

    function del() {
        var checkeds = $('#version_grid').datagrid('getChecked');

        if (checkeds.length == 0) {
            $.messager.alert('错误', '至少选择一项！');
            return;
        }

        var ids = [];
        var desc = '[';

        checkeds.forEach(function (checked) {
            ids.push(checked.id);
            desc = desc + checked.friendlyModel + '('+ checked.iosVersion +'),';
        });

        desc = desc.substr(0, desc.length - 1) + ']';

        $.messager.confirm('确认', '<span style="color: red;">将要删除: </br>' + desc + '.</span>', function (r) {
            if (r) {
                $.ajax({
                    method: 'post',
                    url: 'service/management/version/del',
                    data: {
                        id: ids
                    },
                    success: function (data) {
                        if (data.success) {
                            list();
                        } else {
                            $.messager.alert('错误', data.message);
                        }
                    },
                    error: function (data) {
                        $.messager.alert('错误', data);
                    }
                });
            }
        });
    }

    function list() {
        $('#version_grid').datagrid('load');
    }

    function initApps() {
        $('#version_app').combobox({
            url: 'service/management/app/list',
            loadFilter: function (data) {
                var _data = data.data;
                _data.unshift({id: 0, name: '---请选择---'});
                _data[0].selected = true;
                return _data;
            },
            onSelect: function (record) {
                initVersions(record['id'])
            },
            valueField: 'id',
            textField: 'name'
        });
    }

    function initVersions(appId) {
        $('#version_version').combobox({
            url: 'service/management/version/versions',
            queryParams: {
                appId: appId ? appId : 0
            },
            loadFilter: function (data) {
                var _data = data.data.map(function (d) {
                    return {
                        id: d,
                        name: d
                    };
                });
                _data.unshift({id: 0, name: '---请选择---'});
                _data[0].selected = true;
                return _data;
            },
            valueField: 'id',
            textField: 'name'
        });
    }

    function initGrid() {
        $('#version_grid').datagrid({
            url: 'service/management/version/paging',
            loadFilter: function (data) {
                if (data.success) {
                    return data.data;
                }
            }, toolbar: '#version_grid_toolbar',
            columns: [[{
                field: 'id',
                title: 'ID',
                checkbox: true
            }, {
                field: 'group',
                title: '组',
                sortable: true,
                hidden: true
            }, {
                field: 'appName',
                title: '应用名称',
                sortable: true,
                width: 30
            }, {
                field: 'appVersion',
                title: '应用版本',
                sortable: true,
                width: 30
            }, {
                field: 'iosVersion',
                title: '系统版本',
                sortable: true,
                width: 30
            }, {
                field: 'friendlyModel',
                title: '手机型号',
                sortable: true,
                width: 30
            }, {
                field: 'model',
                title: '内部型号',
                sortable: true,
                width: 30
            }, {
                field: 'captureTime',
                title: '捕获时间',
                sortable: true,
                width: 40
            }, {
                field: 'downloadUrl',
                title: '下载地址',
                formatter: function (value, row, index) {
                    return '<a style="color: inherit" onclick="event.stopPropagation()" target="_blank" href = "' + value + '">' + value + '</a>';
                },
                width: 200
            }]],
            rowStyler: function (index, row) {
                var group = row['group'];
                if (group) {
                    if (!exist[group]) {
                        exist[group] = colors.shift();
                    }
                    return 'color: #ffffff;background-color:' + exist[group];
                }
            },
            fitColumns: true,
            pagination: true,
            pageList: [15, 20, 30, 50, 100],
            pageSize: 15
        });
    }

    function load() {
        $('#version_grid').datagrid('load', getParams());
    }

    function getParams() {
        var params = {};
        var appVersion = $('#version_version').combobox('getValue');
        var appId = $('#version_app').combobox('getValue');
        if (appVersion) {
            params['appVersion'] = appVersion;
        }
        if (appId) {
            params['appId'] = appId;
        }

        return params;
    }

    function group() {
        colors = ["#905a3d", "#87843b", "#73b9a2", "#f173ac", "#cbc547", "#228fbd", "#72777b", "#224b8f", "#56452d", "#74905d", "#6950a1", "#c37e00"];
        exist = {};
        var dg = $('#version_grid');
        dg.datagrid('load', $.extend(getParams(), {
            isGroup: true,
            patternId: $('#version_filter').combobox('getValue')
        }));
    }

    function initFilter() {
        $('#version_filter').combobox({
            url: 'service/management/filter/list',
            queryParams: {
                type: 3
            },
            loadFilter: function (data) {
                var _data = data.data;
                _data[0].selected = true;
                return _data;
            },
            valueField: 'id',
            textField: 'name'
        });
    }

    function download() {

        var distinct = 'true';
        var filterId = $('#version_filter').combobox('getValue');
        var p = getParams();
        var params = 'appId=' + p.appId + '&appVersion=' + p.appVersion + '&filterId=' + filterId;

        window.open('service/management/version/download?' + params);
    }

    $.extend(version, {
        init: function () {
            initApps();
            initFilter();
            initGrid();
        },
        del: del,
        load: load,
        group: group,
        download: download
    });
})(jQuery, version);