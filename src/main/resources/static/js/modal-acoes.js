$('#confirmaExclusaoModal').on('show.bs.modal', function (event) {
	var botao = $(event.relatedTarget);
	var nome = botao.data('nome');
	var url = botao.data('url-apagar');
	var modal = $(this);
	var form = modal.find('form');
	form.attr('action', url);
	modal.find('.modal-body span').html('Tem certeza que deseja excluir o(a) <strong>' + nome + '</strong>?');
});

$('#confirmaEdicaoModal').on('show.bs.modal', function (event) {
	var botao = $(event.relatedTarget);
	var nome = botao.data('nome');
	var url = botao.data('url-editar');
	var modal = $(this);
	var form = modal.find('form');
	form.attr('action', url);
	modal.find('.modal-body span').html('Tem certeza que deseja editar o(a) <strong>' + nome + '</strong>?');
});

$('#confirmaCriarExecOrgaoModal').on('show.bs.modal', function (event) {
	var botao = $(event.relatedTarget);
	var nome = botao.data('nome');
	var url = botao.data('url-criarExecOrgao');
	
	var modal = $(this);
	var form = modal.find('form');
	form.attr('action', url);
	modal.find('.modal-body span').html('Tem certeza que deseja criar novo Exercício Orgão para <strong>' + nome + '</strong>?');
});


