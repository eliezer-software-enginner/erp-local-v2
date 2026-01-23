package my_app.screens.fornecedorScreen;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import megalodonte.*;
import megalodonte.async.Async;
import megalodonte.base.UI;
import megalodonte.components.*;
import megalodonte.components.inputs.Input;
import megalodonte.props.*;
import megalodonte.router.Router;
import megalodonte.styles.ColumnStyler;
import megalodonte.theme.Theme;
import megalodonte.theme.ThemeManager;
import megalodonte.utils.related.TextVariant;
import my_app.db.dto.FornecedorDto;
import my_app.db.models.CategoriaModel;
import my_app.db.models.FornecedorModel;
import my_app.db.repositories.FornecedorRepository;
import my_app.screens.components.Components;
import my_app.utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

import static my_app.utils.Utils.*;

public class FornecedorScreen {
    private final Router router;
    private final Theme theme = ThemeManager.theme();
    private final FornecedorRepository fornecedorRepository = new FornecedorRepository();

    private final ObservableList<FornecedorModel> fornecedores = FXCollections.observableArrayList();

    State<Boolean> editMode = State.of(false);
    ComputedState<String> btnText = ComputedState.of(()-> editMode.get()? "Atualizar" : "Adicionar", editMode);

    State<String> nome = State.of("");
    State<String> cnpj = State.of("");
    State<String> celular = State.of("");
    State<String> inscricaoEstadual = State.of("");
    State<String> email = State.of("");

    //endereço
    List<String> ufList = List.of(
            "AC-Acre", "AL-Alagoas", "AP-Amapá", "AM-Amazonas", "BA-Bahia", "CE-Ceará", "DF-Distrito Federal", "ES-Espírito Santo",
            "GO-Goiás", "MA-Maranhão", "MT-Mato Grosso", "MS-Mato Grosso do Sul", "MG-Minas Gerais", "PA-Pará", "PB-Paraíba", "PR-Paraná",
            "PE-Pernambuco", "PI-Piauí", "RJ-Rio de Janeiro", "RN-Rio Grande do Norte", "RS-Rio Grande do Sul", "RO-Rondônia", "RR-Roraima",
            "SC-Santa Catarina", "SP-São Paulo", "SE-Sergipe", "TO-Tocantins"
    );

    State<String> ufSelected = State.of(ufList.getFirst());
    State<String> cidade = State.of("");
    State<String> bairro = State.of("");
    State<String> rua = State.of("");
    State<String> numero = State.of("");

    State<String> observacao = State.of("");

    State<FornecedorModel> fornecedorSelected = State.of(null);

    public FornecedorScreen(Router router) {
        this.router = router;
    }

    public void onMount(){
        loadFornecedores();
    }

    private void loadFornecedores() {
        Async.Run(()->{
            try {
                fornecedores.clear();
                final var list = fornecedorRepository.listar();
                UI.runOnUi(()->  fornecedores.addAll(list));
            } catch (Exception e) {
                UI.runOnUi(()->   Components.ShowAlertError("Erro ao carregar fornecedores: " + e.getMessage()));

            }
        });
    }


    public Component render() {
        var mainContent = new Column()
          .c_child(form())
          .c_child(new SpacerVertical(30))
          .c_child(table());

        return new Column(new ColumnProps().paddingAll(10), new ColumnStyler().bgColor(theme.colors().background()))
                .c_child(Components.commonCustomMenus(this::clearForm,
                       this::handleClickMenuEdit,
                       this::handleClickMenuDelete
                ))
                .c_child(new SpacerVertical(10))
                .c_child(Components.ScrollPaneDefault(mainContent));
    }



    Component form() {
        return new Card(
                new Column(new ColumnProps().paddingAll(20))
                        .c_child(new Row(new RowProps().centerHorizontally())
                                .r_child(new Text("Cadastro de Fornecedor", new TextProps().bold().variant(TextVariant.SUBTITLE))))
                        .c_child(new SpacerVertical(20))
                        .c_child(new Row(new RowProps().bottomVertically().spacingOf(10))
                                .r_child(Components.InputColumn("Nome Fantasia", nome,"Ex: Empresa 123"))
                                .r_child(Components.InputColumnNumeric("CNPJ", cnpj, ""))
                                .r_child(Components.InputColumnPhone("Celular", celular))
                                .r_child(Components.InputColumn("Inscrição estadual", inscricaoEstadual, ""))
                        )
                        .c_child(Components.InputColumn("Email", email, "Ex: email@teste.com"))
                        .c_child(new SpacerVertical(20))
                        .c_child(new Column().c_child(Components.FormTitle("Endereço")))
                        .c_child(new Row(new RowProps().bottomVertically().spacingOf(10))
                                .r_child(Components.InputColumn("Cidade", cidade,""))
                                .r_child(Components.InputColumn("Bairro", bairro, ""))
                                .r_child(Components.InputColumn("Rua", rua, ""))
                                .r_child(Components.InputColumnNumeric("Número", numero, ""))
                        )
                        .c_child(Components.SelectColumn("UF", ufList, ufSelected, it->it))
                        .c_child(new SpacerVertical(20))
                        .c_child(new LineHorizontal())
                        .c_child(Components.TextAreaColumn("Observação", observacao,""))
                        .c_child(Components.ButtonCadastro(btnText, this::handleAddOrUpdate))


        );
    }

    private void handleClickMenuEdit() {
        final var forn = fornecedorSelected.get();
        if(forn != null){
            editMode.set(true);
            nome.set(forn.nome);
            cnpj.set(forn.cpfCnpj);
            celular.set(forn.celular);
            inscricaoEstadual.set(forn.inscricaoEstadual);
            email.set(forn.email);
            ufSelected.set(forn.ufSelected);
            cidade.set(forn.cidade);
            bairro.set(forn.bairro);
            rua.set(forn.rua);
            numero.set(forn.numero);
            observacao.set(forn.observacao);
        }
    }

    private void handleClickMenuDelete() {
        final var forn = fornecedorSelected.get();
        if(forn != null){
            editMode.set(false);

            Components.ShowAlertAdvice("Deseja excluir fornecedor  " + forn.nome, ()->{
                Async.Run(()->{
                    try{
                        fornecedorRepository.excluirById(forn.id);
                        UI.runOnUi(()->{
                            fornecedores.removeIf(it-> it.id.equals(forn.id));
                            Components.ShowPopup(router, "Fornecedor excluido com sucesso");
                        });
                    }catch (Exception e){
                        UI.runOnUi(()->Components.ShowAlertError("Erro ao tentar excluir: " + e.getMessage()));
                    }
                });
            });


        }
    }

    private void clearForm(){
        editMode.set(false);
        nome.set("");
        cnpj.set("");
        celular.set("");
        inscricaoEstadual.set("");
        email.set("");
        ufSelected.set(ufList.getFirst());
        cidade.set("");
        bairro.set("");
        rua.set("");
        numero.set("");
        observacao.set("");
    }

    private void handleAddOrUpdate() {
        String nomeValue = nome.get().trim();
        String cnpjValue = cnpj.get().trim();
        String celularValue = celular.get().trim();
        String emailValue = email.get().trim();
        String inscricaoValue = inscricaoEstadual.get().trim();
        String ufValue = ufSelected.get().trim();
        String cidadeValue = cidade.get().trim();
        String bairroValue = bairro.get().trim();
        String ruaValue = rua.get().trim();
        String numeroValue = numero.get().trim();
        String observacaoValue = observacao.get().trim();

        if (nomeValue.isEmpty()) {
            Components.ShowAlertError("Nome é obrigatório");
            return;
        }

        if (!cnpjValue.isEmpty() && !isValidCnpj(cnpjValue)) {
            Components.ShowAlertError("CNPJ inválido (deve conter 14 dígitos)");
            return;
        }

        // 3. Validação de E-mail (se preenchido)
        if (!emailValue.isEmpty() && !isValidEmail(emailValue)) {
            Components.ShowAlertError("Formato de e-mail inválido");
            return;
        }

        // 4. Validação de Telefone/Celular
        if (!celularValue.isEmpty() && !isValidPhone(celularValue)) {
            Components.ShowAlertError("Telefone inválido (informe DDD + Número)");
            return;
        }

        if(editMode.get() && fornecedorSelected.get() == null) return;

        if(editMode.get()){
            Async.Run(()->{
                try {
                    // 1. Criamos a Model com os novos dados mantendo o ID e Data de Criação originais
                    FornecedorModel selecionado = fornecedorSelected.get();
                    FornecedorModel modelAtualizada = new FornecedorModel().fromIdAndDto(selecionado.id, new FornecedorDto(
                            nomeValue, cnpjValue, celularValue, emailValue,
                            inscricaoValue, ufValue, cidadeValue, bairroValue,
                            ruaValue, numeroValue, observacaoValue
                    ));

                    // 2. Atualiza no Banco de Dados
                    fornecedorRepository.atualizar(modelAtualizada);

                    UI.runOnUi(() -> {
                        // 3. Atualiza na ObservableList
                        int index = fornecedores.indexOf(selecionado);
                        if (index != -1) {
                            fornecedores.set(index, modelAtualizada);
                        }

                        Components.ShowPopup(router, "Fornecedor atualizado com sucesso");
                        clearForm();
                    });

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }else{
            Async.Run(()->{
                try {
                    var dto = new FornecedorDto(
                            nomeValue,
                            cnpjValue,
                            celularValue,
                            emailValue,
                            inscricaoValue,
                            ufValue,
                            cidadeValue,
                            bairroValue,
                            ruaValue,
                            numeroValue,
                            observacaoValue
                    );

                    var model = fornecedorRepository.salvar(dto);

                    UI.runOnUi(()-> {
                        fornecedores.add(model);
                        Components.ShowPopup(router, "Fornecedor cadastrado com sucesso");
                        clearForm();
                    });

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }



    }

    Component table() {
        TableView<FornecedorModel> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Coluna ID
        TableColumn<FornecedorModel, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                    data.getValue().id != null ? String.valueOf(data.getValue().id) : ""
                )
        );
        idCol.setMaxWidth(40);

        // Coluna Nome
        TableColumn<FornecedorModel, String> nomeCol = new TableColumn<>("Nome");
        nomeCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().nome)
        );
        nomeCol.setMaxWidth(100);

        // Coluna CNPJ
        TableColumn<FornecedorModel, String> cnpjCol = new TableColumn<>("CNPJ");
        cnpjCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().cpfCnpj)
        );

        TableColumn<FornecedorModel, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().email)
        );

        TableColumn<FornecedorModel, String> telefoneCol = new TableColumn<>("Telefone");
        telefoneCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(Utils.formatPhone(data.getValue().celular))
        );

        //cnpjCol.setMaxWidth(100);

        // Coluna Data Criação
        TableColumn<FornecedorModel, String> dataCol = new TableColumn<>("Data de Criação");
        dataCol.setCellValueFactory(data -> {
            if (data.getValue().dataCriacao != null) {
                return new javafx.beans.property.SimpleStringProperty(
                        Utils.formatDateTime(data.getValue().dataCriacao)
                );
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });

        table.getColumns().addAll(idCol, nomeCol, emailCol, telefoneCol, cnpjCol, dataCol);
        table.setItems(fornecedores);

        Utils.onItemTableSelectedChange(table, data-> fornecedorSelected.set(data));

        return Component.CreateFromJavaFxNode(table);
    }
}