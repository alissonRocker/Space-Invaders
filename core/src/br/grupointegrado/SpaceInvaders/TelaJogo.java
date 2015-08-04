package br.grupointegrado.SpaceInvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FillViewport;

public class TelaJogo extends TelaBase {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Stage palco;
    private BitmapFont fonte;
    private Label lbPontuacao;

    /**
     * Construtor padrão da tela de jogo.
     * @param game Referência para a classe Principal.
     */
    public TelaJogo(MainGame game) {
        super(game);
    }

    /**
     * Chamado quando a tela é exibida.
     */
    @Override
    public void show() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // inicia e passa dados da tela.
        batch = new SpriteBatch();
        palco = new Stage(new FillViewport(camera.viewportWidth, camera.viewportHeight, camera)); // instância e liga palco na camera;

        initFonte();
        initInformacoes();
    }

    private void initFonte() {
        fonte = new BitmapFont();
    }

    private void initInformacoes() {
        Label.LabelStyle lbEstilo = new Label.LabelStyle();
        lbEstilo.font = fonte; // passa objeto fonte.
        lbEstilo.fontColor = Color.WHITE;

        lbPontuacao = new Label("0 pontos", lbEstilo);
        palco.addActor(lbPontuacao); // adiciona no palco.
    }

    /**
     * Chamado a todo a quadro de atualização do jogo (FPS - Quadros por segundos)
     * @param delta Tempo entre um campo e outro (em segundos)
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.15f, .15f, .25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Limpa buffer de cores.

        lbPontuacao.setPosition(10, camera.viewportHeight - 20); // altura mazima da camera - 20.

        palco.act(delta); // avisa que ja passou o quadro com determinados segundos.
        palco.draw();
    }

    /**
     * Chamado sempre que há uma alteração no tamanho da tela.
     * @param width Novo valor de largura da tela.
     * @param height Novo valor de altura da tela.
     */
    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.update();
    }

    /**
     * Chamado sempre que o jogo for minimizado (segundo plano).
     */
    @Override
    public void pause() {

    }

    /**
     * É chamado sempre que o jogo voltar para o primeiro plano.
     */
    @Override
    public void resume() {

    }

    /**
     * Chamado quando a tela for destruida.
     */
    @Override
    public void dispose() {
        batch.dispose();
        palco.dispose();
        fonte.dispose();
    }


}
