/**
 * Created by Neo on 2017/2/10.
 */

var collect = collect || {};

(function ($, collect) {
    function initGrid() {
        $('#collect_grid').datagrid({
            singleSelect: true,
            toolbar: '#collect_grid_toolbar',
            columns: [[{
                field: 'friendlyModel',
                title: '手机型号',
                width: 30
            }, {
                field: 'iosVersion',
                title: '系统版本',
                width: 30
            }, {
                field: 'model',
                title: '内部型号',
                width: 30
            }, {
                field: 'captureTime',
                title: '捕获时间',
                width: 30
            }, {
                field: 'downloadUrl',
                title: '下载地址',
                width: 200
            }]],
            fitColumns: true
        });
    }

    function expandDevGrid() {
        $('#collect_devices_panel').panel('expand', true);
        $('#collect_panel').panel('collapse', true);
    }

    function initDevGrid() {
        $('#collect_devices_grid').datagrid({
            url: 'service/management/collect/devices',
            loadFilter: function (data) {
                if (data.success) {
                    return data.data;
                }
            },
            onClickRow: showIp,
            singleSelect: true,
            toolbar: [{
                text: '选择',
                iconCls: 'icon-ok',
                handler: selectDevice
            }, {
                text: '刷新',
                iconCls: 'icon-reload',
                handler: function () {
                    $('#collect_devices_grid').datagrid('load');
                }
            }],
            columns: [[{
                field: 'id',
                title: 'ID',
                checkbox: true
            }, {
                field: 'name',
                title: '名称',
                width: 200
            }, {
                field: 'mac_address',
                title: 'MAC地址',
                width: 80
            }, {
                field: 'description',
                title: '描述',
                width: 200
            }, {
                field: 'datalink_name',
                title: 'DataLink名称',
                width: 100
            }, {
                field: 'datalink_description',
                title: 'DataLink描述',
                width: 100
            }]],
            fitColumns: true
        });
    }

    function selectDevice() {
        var checkeds = $('#collect_devices_grid').datagrid('getChecked');
        if (checkeds.length != 1) {
            $.messager.alert('错误', '至少选择一项');
            return;
        }
        $('#collect_devices_ips').window('close');
        $('#collect_device_selected').textbox('setValue', checkeds[0]['name']);
        $('#collect_device_selected_id').val(checkeds[0]['id']);
        $('#collect_devices_panel').panel('collapse', true);
        $('#collect_panel').panel('expand', true);
    }

    function selectApp(id, name, icon_url, version) {
        layout.openTab(layout.functions.COLLECT, function () {
            $('#collect_app_id').val(id);
            $('#collect_app_name').text(name);
            $('#collect_app_version').text(version);
            $('#collect_app_icon').attr('src', icon_url);
        });
    }

    function init() {
        initDevGrid();
        initGrid();
        initFilter();
    }

    function initFilter() {
        $('#collect_filter').combobox({
            url: 'service/management/filter/list',
            queryParams: {
                type: 1
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

    function showIp(index, row) {
        $('#collect_devices_ip_grid').datagrid('loadData', row['addresss']);
        var ipWin = $('#collect_devices_ips');
        if (ipWin.window('options')['closed']) {
            ipWin.window('open');
        }
    }


    function startCollect() {
        var deviceId = $('#collect_device_selected_id').val();
        if (!deviceId) {
            $.messager.alert('错误', '请选择设备');
            return;
        }
        var appId = $('#collect_app_id').val();
        if (!appId) {
            $.messager.alert('错误', '请选择应用');
            return
        }

        var filterId = $('#collect_filter').combobox('getValue');
        if (!filterId) {
            $.messager.alert('错误', '请选择过滤器');
            return;
        }

        $.ajax({
            method: 'post',
            url: 'service/management/collect/start',
            data: {
                dev: deviceId,
                filter: filterId
            },
            success: function (data) {
                if (data.success) {
                    $('#collect_grid').datagrid('loadData', []);
                    collect.tmInterval = setInterval(function () {
                        var tm = $('#collect_status_time');
                        if (tm.text) {
                            tm.text(parseInt(tm.text()) + 1);
                        } else {
                            tm.text(0);
                        }
                    }, 1000);
                    $('#collect_status_msg').css('visibility', 'visible');
                    $('#collect_save').linkbutton('disable');
                    $('#collect_download').linkbutton('disable');
                    _startCollect();
                } else {
                    $.messager.alert('错误', data.message);
                }
            }
        });
    }

    function _startCollect(signature) {
        if (!signature) {
            signature = 0;
        }
        $.ajax({
            method: 'post',
            url: 'service/management/collect/new',
            data: {
                last: signature
            },
            success: function (data) {
                if (data.success) {
                    var _data = data.data;
                    if (_data) {
                        $('#collect_grid').datagrid('loadData', _data);
                        $('#collect_grid').datagrid('scrollTo', _data.length - 1);
                        $('#collect_status_count').text(_data.length);
                        signature = _data[_data.length - 1]['signature'];
                    }
                    _startCollect(signature);
                } else {
                    console.info(data.message)
                }
            }
        });
    }

    function stopCollect() {
        var deviceId = $('#collect_device_selected_id').val();
        if (!deviceId) {
            $.messager.alert('错误', '请选择设备');
            return
        }
        $.ajax({
            url: 'service/management/collect/stop',
            method: 'post',
            data: {
                dev: deviceId
            },
            success: function (data) {
                if (data.success) {
                    clearInterval(collect.tmInterval);
                    $('#collect_status_count').text(0);
                    $('#collect_status_msg').css('visibility', 'hidden');
                    $('#collect_status_time').text(0);
                    $('#collect_save').linkbutton('enable');
                    $('#collect_download').linkbutton('enable');
                } else {
                    $.messager.alert('错误', data.message);
                }
            }
        });
    }

    function save() {
        $.ajax({
            url: 'service/management/collect/save',
            data: {
                appId: $('#collect_app_id').val()
            },
            method: 'post',
            success: function (data) {
                if (data.success) {
                    $.messager.alert('成功', '保存成功');
                } else {
                    $.messager.alert('错误', data.message);
                }
            }
        });
    }

    $.extend(collect, {
        selectApp: selectApp,
        init: init,
        expandDevGrid: expandDevGrid,
        startCollect: startCollect,
        stopCollect: stopCollect,
        save: save
    });
})(jQuery, collect);