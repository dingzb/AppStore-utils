/**
 * Created by Neo on 2017/2/4.
 */

var app = app || {};

(function ($, app) {

    function addWindow() {
        $('#app_add_agency_id').combobox({
            url: 'service/management/agency/list',
            loadFilter: function (data) {
                if (data.success) {
                    data.data.unshift({
                        id: '',
                        name: '--请选择--',
                        selected: true
                    });
                    return data.data;
                }
            },
            valueField: 'id',
            textField: 'name',
            required: true,
            novalidate: true,
            validType: 'unequals["--请选择--"]',
            editable: false
        });
        $('#app_add').window('open');
    }

    function add() {
        if (!validate('add')) {
            return;
        }
        $.ajax({
            method: 'post',
            url: 'service/management/app/add',
            data: {
                name: $('#app_add_name').textbox('getValue'),
                version: $('#app_add_version').textbox('getValue'),
                infoUrl: $('#app_add_info_url').textbox('getValue'),
                agencyId: $('#app_add_agency_id').combobox('getValue')
            },
            success: function (data) {
                if (data.success) {
                    $('#app_add').window('close');
                    list('load');
                    clear('add');
                    disableValidation();
                } else {
                    $.messager.alert('错误', data.message);
                }
            },
            error: function (data) {
                $.messager.alert('错误', data);
            }
        });
    }

    function validate(type) {
        return $('#app_' + type + '_info_url')
                .textbox('enableValidation')
                .textbox('validate')
                .textbox('isValid') & $('#app_' + type + '_agency_id')
                .combobox('enableValidation')
                .combobox('validate')
                .combobox('isValid');
    }

    function disableValidation(type) {
        $('#app_' + type + '_info_url').textbox('disableValidation');
        $('#app_' + type + '_agency_id').textbox('disableValidation');
    }

    function del() {
        var checkeds = $('#app_grid').datagrid('getChecked');

        if (checkeds.length == 0) {
            $.messager.alert('错误', '至少选择一项！');
            return;
        }

        var ids = [];
        var names = '[';

        checkeds.forEach(function (checked) {
            ids.push(checked.id);
            names = names + checked.name + ",";
        });

        names = names.substr(0, names.length - 1) + ']';

        $.messager.confirm('确认', '<span style="color: red;">将要删除: </br>' + names + '.</span>', function (r) {
            if (r) {
                $.ajax({
                    method: 'post',
                    url: 'service/management/app/del',
                    data: {
                        id: ids
                    },
                    success: function (data) {
                        if (data.success) {
                            list('load');
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

    function edit() {
        var checkeds = $('#app_grid').datagrid('getChecked');

        if (checkeds.length != 1) {
            $.messager.alert('错误', '至少或只能选择一项！');
            return;
        }

        if (!validate('edit')) {
            return;
        }
        $.ajax({
            method: 'post',
            url: 'service/management/app/edit',
            data: {
                id: checkeds[0].id,
                name: $('#app_edit_name').textbox('getValue'),
                version: $('#app_edit_version').textbox('getValue'),
                infoUrl: $('#app_edit_info_url').textbox('getValue'),
                agencyId: $('#app_edit_agency_id').combobox('getValue')
            },
            success: function (data) {
                if (data.success) {
                    $('#app_edit').window('close');
                    list('reload');
                    clear('edit');
                    disableValidation('edit');
                } else {
                    $.messager.alert('错误', data.message);
                }
            },
            error: function (data) {
                $.messager.alert('错误', data);
            }
        });

    }

    function editWindow() {

        var checkeds = $('#app_grid').datagrid('getChecked');

        if (checkeds.length != 1) {
            $.messager.alert('错误', '至少或只能选择一项！');
            return;
        }

        $('#app_edit_agency_id').combobox({
            url: 'service/management/agency/list',
            loadFilter: function (data) {
                if (data.success) {
                    data.data.forEach(function (d) {
                        if (d.id == checkeds[0].agencyId) {
                            d.selected = true;
                        }
                    });
                    data.data.unshift({
                        id: '',
                        name: '--请选择--'
                    });
                    return data.data;
                }
            },
            valueField: 'id',
            textField: 'name',
            required: true,
            novalidate: true,
            validType: 'unequals["--请选择--"]',
            editable: false
        });

        $('#app_edit_name').textbox('setValue', checkeds[0].name);
        $('#app_edit_version').textbox('setValue', checkeds[0].version);
        $('#app_edit_info_url').textbox('setValue', checkeds[0].infoUrl);
        $('#app_edit').window('open');
    }

    function clear(type) {
        $('#app_' + type + '_name').textbox('clear');
        $('#app_' + type + '_version').textbox('clear');
        $('#app_' + type + '_info_url').textbox('clear');
        $('#app_' + type + '_agency_id').combobox('reset');
    }

    function list(type) {
        $('#app_grid').datagrid(type);
    }

    function initGrid() {
        $('#app_grid').datagrid({
            url: 'service/management/app/paging',
            loadFilter: function (data) {
                if (data.success) {
                    return data.data;
                }
            },
            pagination: true,
            toolbar: '#app_grid_toolbar',
            columns: [[{
                field: 'id',
                title: 'ID',
                checkbox: true
            }, {
                field: 'iconUrl',
                title: '图标',
                formatter: function (value, row, index) {
                    return '<a onclick="event.stopPropagation()" target="_blank" href="' + row['infoUrl'] + '">' +
                        '<img style="margin-top: 3px; margin-left: 2px; width: 32px; height: 32px; border-radius: 6px;" src="' + (value ? value : 'img/app.png') + '"/>' +
                        '</a>';
                },
                width: 28
            }, {
                field: 'name',
                title: '名称',
                sortable: true,
                width: 100
            }, {
                field: 'updated',
                title: '最新版本',
                formatter: function (value, row, index) {
                    return '<span style="display: inline-block; margin-left: -1px; width: 100%; border: 1px solid #e8e8e8; border-radius: 2px; cursor: pointer;" ' +
                        'onclick="app.toggleUpdated(\'' + row['id'] + '\');event.stopPropagation()">' + row['version'] + '</span>';
                },
                styler: function (index, row) {
                    if (!(row['updated'])) {
                        return 'background-color:rgba(255, 228, 141, 0.43);color:#444;';
                    } else {
                        return '';
                    }
                },
                sortable: true,
                width: 100
            }, {
                field: 'agencyId',
                title: '机构',
                formatter: function (value, row, index) {
                    return row['agencyName'];
                },
                sortable: true,
                width: 100
            }, {
                field: 'createTime',
                title: '创建时间',
                sortable: true,
                width: 100
            }, {
                field: 'updateTime',
                title: '更新时间',
                sortable: true,
                width: 100
            }, {
                field: 'versionTime',
                title: '版本时间',
                sortable: true,
                width: 100
            }, {
                field: '_operator',
                title: '操作',
                sortable: true,
                formatter: function (val, ro, index) {
                    var checkBtn = '<span onclick="app.checkUpdate(\'' + ro.id + '\');event.stopPropagation()" title="检查更新" class="grid_row_btn icon-reload">&nbsp;</span>';
                    var collectBtn = '<span onclick="collect.selectApp(\'' + ro.id + '\',\'' + ro.name + '\',\'' + ro.iconUrl + '\',\'' + ro.version + '\');event.stopPropagation()" title="采集应用" class="grid_row_btn icon-search">&nbsp;</span>';
                    return checkBtn + '<span>&nbsp;</span>' + collectBtn;
                },
                width: 100
            }]],
            pageList: [15, 20, 30],
            pageSize: 15,
            fitColumns: true
        });
    }

    function toggleUpdated(id) {
        $.ajax({
            method: 'post',
            url: 'service/management/app/toggleUpdated',
            data: {
                id: id
            },
            success: function (data) {
                if (data.success) {
                    list('reload');
                } else {
                    $.messager.alert('错误', data.message);
                }
            },
            error: function (data) {
                $.messager.alert('错误', data);
            }
        });
    }

    function initFilter() {
        $('#app_filter').combobox({
            url: 'service/management/filter/list',
            queryParams: {
                type: 2
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

    function checkUpdate(id) {
        var ids = [];
        if (!id) {
            var rows = $('#app_grid').datagrid('getChecked');
            if (rows.length < 1) {
                $.messager.alert('错误', '至少选择一项');
                return;
            }
            rows.forEach(function (row) {
                ids.push(row.id);
            });
        } else {
            ids.push(id);
        }

        $.ajax({
            method: 'post',
            url: 'service/management/app/update',
            data: {
                id: ids,
                filterId: $('#app_filter').combobox('getValue')
            },
            success: function (data) {
                if (data.success) {
                    list('reload');
                } else {
                    $.messager.alert('错误', data.message);
                }
            },
            error: function (data) {
                $.messager.alert('错误', data);
            }
        });
    }

    function init() {
        initGrid();
        initFilter();
    }

    $.extend(app, {
        addWindow: addWindow,
        add: add,
        editWindow: editWindow,
        edit: edit,
        clear: clear,
        disableValidation: disableValidation,
        init: init,
        del: del,
        checkUpdate: checkUpdate,
        toggleUpdated: toggleUpdated
    });
})(jQuery, app);