/**
 * Created by Neo on 2017/2/4.
 */

var filter = filter || {};

(function ($, filter) {
    function addWindow() {
        $('#filter_add').window('open');
    }

    function add() {
        if (!validate('add')) {
            return;
        }
        $.ajax({
            method: 'post',
            url: 'service/management/filter/add',
            data: {
                name: $('#filter_add_name').textbox('getText'),
                content: $('#filter_add_content').textbox('getText'),
                type: $('#filter_add_type').combobox('getValue')
            },
            success: function (data) {
                if (data.success) {
                    $('#filter_add').window('close');
                    disableValidation('add');
                    clear('add');
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

    function validate(type) {
        return $('#filter_' + type + '_name')
                .textbox('enableValidation')
                .textbox('validate')
                .textbox('isValid')
            & $('#filter_' + type + '_content')
                .textbox('enableValidation')
                .textbox('validate')
                .textbox('isValid');
    }

    function disableValidation(type) {
        $('#filter_' + type + '_name').textbox('disableValidation');
        $('#filter_' + type + '_content').textbox('disableValidation');
    }

    function del() {
        var checkeds = $('#filter_grid').datagrid('getChecked');

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
                    url: 'service/management/filter/del',
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

    function edit() {
        var checkeds = $('#filter_grid').datagrid('getChecked');

        if (!validate('edit')) {
            return;
        }

        $.ajax({
            method: 'post',
            url: 'service/management/filter/edit',
            data: {
                id: checkeds[0].id,
                name: $('#filter_edit_name').textbox('getText'),
                content: $('#filter_edit_content').textbox('getText'),
                type: $('#filter_edit_type').combobox('getValue')
            },
            success: function (data) {
                if (data.success) {
                    $('#filter_edit').window('close');
                    disableValidation('edit');
                    clear('edit');
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

    function editWindow() {

        var checkeds = $('#filter_grid').datagrid('getChecked');

        if (checkeds.length != 1) {
            $.messager.alert('错误', '至少或只能选择一项！');
            return;
        }

        $('#filter_edit_name').textbox('setText', checkeds[0].name);
        $('#filter_edit_content').textbox('setText', checkeds[0].content);
        $('#filter_edit').window('open');
    }

    function clear(type) {
        $('#filter_' + type + '_name').textbox('clear');
        $('#filter_' + type + '_content').textbox('clear');
    }

    function list() {
        $('#filter_grid').datagrid('load');
    }

    function initGrid() {
        $('#filter_grid').datagrid({
            url: 'service/management/filter/list',
            loadFilter: function (data) {
                if (data.success) {
                    return data.data;
                }
            }, toolbar: [{
                text: '添加',
                iconCls: 'icon-add',
                handler: addWindow
            }, {
                text: '编辑',
                iconCls: 'icon-edit',
                handler: editWindow
            }, {
                text: '删除',
                iconCls: 'icon-remove',
                handler: del
            }, '-', {
                iconCls: 'icon-help',
                handler: function () {
                    alert('help')
                }
            }],
            columns: [[{
                field: 'id',
                title: 'ID',
                checkbox: true
            }, {
                field: 'name',
                title: '名称',
                width: 50
            }, {
                field: 'type',
                title: '类型',
                formatter: function (value, row, index) {
                    switch (value) {
                        case 1:
                            return '采集规则';
                        case 2:
                            return '更新规则';
                        case 3:
                            return 'URL甄别';
                        default:
                            return value;
                    }
                },
                width: 30
            }, {
                field: 'content',
                title: '内容',
                formatter: function (value, row, index) {
                    value = value.replace(/</g, "&lt;").replace(/>/g, "&gt;");
                    return '<div style="white-space: pre-line">' + value + '</div>';
                },
                width: 280
            }, {
                field: 'createTime',
                title: '创建时间',
                width: 40
            }, {
                field: 'updateTime',
                title: '更新时间',
                width: 40
            }]],
            fitColumns: true
        });
    }

    $.extend(filter, {
        add: add,
        edit: edit,
        clear: clear,
        init: initGrid,
        disableValidation: disableValidation
    });
})(jQuery, filter);