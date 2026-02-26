package my_app.screens;

import megalodonte.Show;
import megalodonte.State;
import megalodonte.base.UI;
import megalodonte.base.async.Async;
import megalodonte.components.*;
import megalodonte.props.ColumnProps;
import megalodonte.router.Router;
import my_app.db.models.PreferenciasModel;
import my_app.db.repositories.PreferenciasRepository;
import my_app.screens.components.Components;

import java.sql.SQLException;

public class AuthScreen implements ScreenComponent {
    private final Router router;
    private final PreferenciasRepository preferenciasRepository;

    State<Boolean> showLicensaState = State.of(true);

    State<String> licensaState = State.of("");
    State<String> loginState = State.of("");
    State<String> passwordState = State.of("");

    PreferenciasModel prefRecuperada;

    public AuthScreen(Router router) {
        this.router = router;
        preferenciasRepository = new PreferenciasRepository();
    }

    @Override
    public void onMount() {
        Async.Run(()->{
            try{
                var prefs = preferenciasRepository.listar();
                if(!prefs.isEmpty()){
                    prefRecuperada = prefs.getFirst();
                        UI.runOnUi(()-> showLicensaState.set(prefRecuperada.isFirstAccess()));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Component render() {
        return new Column(new ColumnProps().paddingAll(20)).children(
                new Text("Realiza já seu login na Plics SW"),
                Show.when(showLicensaState,  ()-> Components.InputColumn("Licensa", licensaState, "")),
                        Components.InputColumn("Login", loginState, ""),
                        Components.InputColumn("Senha", passwordState, ""),
                new SpacerVertical(10),
                Components.ButtonCadastro("Entrar", this::entrar)
        );
    }

    void entrar(){
        var licensaValue = licensaState.get().trim();
        var licensaBase = "984e2bb76c7b627641b6b7dc080f8e23";

        if(showLicensaState.get() && (licensaValue.isEmpty() || !licensaValue.equals(licensaBase))){
            Components.ShowAlertError("Licensa inválida");
            return;
        }

        String loginValue = loginState.get().trim();
        String senhaValue = passwordState.get().trim();

        if(!prefRecuperada.login.trim().equals(loginValue) || !prefRecuperada.senha.trim().equals(senhaValue)){
            Components.ShowAlertError("Login inválido");
            return;
        }

        Components.ShowPopup(router, "Login efetuado com sucesso!");
            try {
                var prefs = new PreferenciasRepository().listar();
                if (!prefs.isEmpty()) {
                    var pref = prefs.getFirst();
                    pref.primeiroAcesso = 0;
                    pref.credenciaisHabilitadas = 1;
                    new PreferenciasRepository().atualizar(pref);
                    router.navigateTo("home");
                }
            } catch (Exception e) {
                e.printStackTrace();
                UI.runOnUi(()->   Components.ShowAlertError("Erro ao entrar: " + e.getMessage()));
            }

    }
}