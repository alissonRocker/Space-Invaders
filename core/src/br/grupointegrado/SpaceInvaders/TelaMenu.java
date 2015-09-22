package br.grupointegrado.SpaceInvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;

public class TelaMenu extends TelaBase {

    private OrthographicCamera camera;
    private Stage palco;
    private ImageTextButton btIniciar;
    private Label lbTitulo;
    private Label lbPontuacao;

    private BitmapFont fontTitulo;
    private BitmapFont fontBotoes;

    private Texture texturaBotao;
    private Texture texturaBotaoPressionado;

    public TelaMenu(MainGame game) { super(game); }

    @Override
    public void show() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        palco = new Stage(new FillViewport(camera.viewportWidth, camera.viewportHeight, camera));

        Gdx.input.setInputProcessor(palco); // Define o palco como processador de entradas.

        initFontes();
        initLabels();
        initBotoes();
    }

    private void initFontes() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/roboto.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();

        param.color = new Color(.25f, .25f, .85f, 1); // tom de azul.
        param.size = 48;
        param.shadowOffsetX = 2;
        param.shadowOffsetY = 2;
        param.shadowColor = Color.BLACK;

        fontTitulo = generator.generateFont(param);

        // instância a fonte dos botoes.
        param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = 32;
        param.color = Color.BLACK;

        fontBotoes = generator.generateFont(param);

        generator.dispose();
    }

    private void initLabels() {
        // cria o label do titulo.
        Label.LabelStyle estilo = new Label.LabelStyle();
        estilo.font = fontTitulo;

        lbTitulo = new Label("Space Invaders", estilo);
        palco.addActor(lbTitulo);

        Preferences preferencias = Gdx.app.getPreferences("SpaceInvaders");
        int pontuacoMaxima = preferencias.getInteger("pontuacao_maxima", 0);

        // cria o label da pontuação maxima.
        estilo = new Label.LabelStyle();
        estilo.font = fontBotoes;

        lbPontuacao = new Label("Pontuação máxima: " + pontuacoMaxima + " ponto(s)", estilo);
        palco.addActor(lbPontuacao);
    }

    private void initBotoes() {
        texturaBotao = new Texture("buttons/button.png");
        texturaBotaoPressionado = new Texture("buttons/button-down.png");

        ImageTextButton.ImageTextButtonStyle estilo = new ImageTextButton.ImageTextButtonStyle();

        estilo.font = fontBotoes;
        estilo.up = new SpriteDrawable(new Sprite(texturaBotao));
        estilo.down = new SpriteDrawable(new Sprite(texturaBotaoPressionado));

        btIniciar = new ImageTextButton(" Iniciar Jogo ", estilo);
        palco.addActor(btIniciar);

        btIniciar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // evento de clique no botão.
                game.setScreen(new TelaJogo(game));
            }
        } );
    }

    private void atualizarLabels() {
        float x = camera.viewportWidth / 2 - lbTitulo.getPrefWidth() / 2;
        float y = camera.viewportHeight - 100;

        lbTitulo.setPosition(x, y);

        x = camera.viewportWidth / 2 - lbPontuacao.getPrefWidth() / 2;
        y = 100;

        lbPontuacao.setPosition(x, y);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        atualizarLabels();
        atualizarBotoes();

        palco.act(delta);
        palco.draw();
    }

    private void atualizarBotoes() {
        float x = camera.viewportWidth / 2 - btIniciar.getPrefWidth() / 2;
        float y = camera.viewportHeight / 2 - btIniciar.getPrefHeight() / 2;

        btIniciar.setPosition(x, y);
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.update();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        palco.dispose();
        fontTitulo.dispose();

        fontBotoes.dispose();

        texturaBotao.dispose();
        texturaBotaoPressionado.dispose();
    }

}
