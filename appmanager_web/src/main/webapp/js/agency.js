/**
 * Created by Neo on 2017/2/4.
 */

var agency = agency || {};

(function ($, agency) {
    function addWindow() {
        $('#agency_add').window('open');
    }

    function add() {
        var addWin = $('#agency_add');
        var name = $('#agency_add_name');
        if (!validate('add')) {
            return;
        }
        $.ajax({
            method: 'post',
            url: 'service/management/agency/add',
            data: {
                name: name.textbox('getText')
            },
            success: function (data) {
                if (data.success) {
                    addWin.window('close');
                    list();
                    clear('add');
                    disableValidation('add');
                } else {
                    $.messager.alert('错误', data.message);
                }
            },
            error: function (data) {
                $.messager.alert('错误', data);
            }
        });
    }

    function del() {
        var checkeds = $('#agency_grid').datagrid('getChecked');

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
                    url: 'service/management/agency/del',
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
        var checkeds = $('#agency_grid').datagrid('getChecked');

        if (checkeds.length != 1) {
            $.messager.alert('错误', '至少或只能选择一项！');
            return;
        }

        var id = checkeds[0].id;
        var name = $('#agency_edit_name').textbox('getText');
        if (!validate('edit')) {
            return;
        }
        $.ajax({
            method: 'post',
            url: 'service/management/agency/edit',
            data: {
                id: id,
                name: name
            },
            success: function (data) {
                if (data.success) {
                    $('#agency_edit').window('close');
                    list();
                    clear('edit');
                    disableValidation('edit')
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

        var checkeds = $('#agency_grid').datagrid('getChecked');

        if (checkeds.length != 1) {
            $.messager.alert('错误', '至少或只能选择一项！');
            return;
        }

        var name = checkeds[0].name;

        $('#agency_edit_name').textbox('setText', name);
        $('#agency_edit').window('open');
    }

    function clear(type) {
        $('#agency_' + type + "_name").textbox('clear');
    }

    function list() {
        $('#agency_grid').datagrid('load');
    }

    function validate(type) {
        return $('#agency_' + type + '_name')
            .textbox('enableValidation')
            .textbox('validate')
            .textbox('isValid');
    }

    function disableValidation(type) {
        $('#agency_' + type + '_name').textbox('disableValidation');
    }

    function initGrid() {
        $('#agency_grid').datagrid({
            url: 'service/management/agency/list',
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
                width: 100
            }, {
                field: 'createTime',
                title: '创建时间',
                width: 100
            }, {
                field: 'updateTime',
                title: '更新时间',
                width: 100
            }]],
            fitColumns: true
        });
    }

    $.extend(agency, {
        add: add,
        edit: edit,
        clear: clear,
        init: initGrid,
        disableValidation: disableValidation
    });
})(jQuery, agency);