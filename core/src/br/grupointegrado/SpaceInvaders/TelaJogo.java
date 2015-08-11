package br.grupointegrado.SpaceInvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;

public class TelaJogo extends TelaBase {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Stage palco;
    private BitmapFont fonte;
    private Label lbPontuacao;
    private Image jogador;
    private Texture texturaJogador;
    private Texture texturaJogadorDireita;
    private Texture texturaJogadorEsquerda;
    private boolean indoDireita;
    private boolean indoEsquerda;

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

        initJogador();
    }

    private void initJogador() {
        texturaJogador = new Texture("sprites/player.png");
        texturaJogadorDireita = new Texture("sprites/player-right.png");
        texturaJogadorEsquerda = new Texture("sprites/player-left.png");

        jogador = new Image(texturaJogador);
        float x = camera.viewportWidth / 2 - jogador.getWidth() / 2;
        float y = 15;
        jogador.setPosition(x, y);
        palco.addActor(jogador);
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
     * @param delta Tempo entre um quadro e outro (em segundos)
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.15f, .15f, .25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Limpa buffer de cores.

        lbPontuacao.setPosition(10, camera.viewportHeight - 20); // altura mazima da camera - 20.

        capturaTeclas();
        atualizarJogador(delta);

        palco.act(delta); // avisa que ja passou o quadro com determinados segundos.
        palco.draw();
    }

    /**
     * Verifica se as teclas estão pressionadas.
     */
    private void capturaTeclas() {
        indoDireita = false;
        indoEsquerda = false;

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) { indoEsquerda = true; }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) { indoDireita = true; }

    }

    /**
     * Atualiza a posição do jogador.
     * @param delta
     */
    private void atualizarJogador(float delta) {
        float velocidade = 200; // Velocidade de movimento do jogador.

        if(indoDireita) {
            if(jogador.getX() < camera.viewportWidth - jogador.getWidth()) {
                float x = jogador.getX() + velocidade * delta; // tempo que passou X velocidade.
                float y = jogador.getY();
                jogador.setPosition(x, y);
            }
        }

        if(indoEsquerda) {
            if(jogador.getX() >= 0) {
                float x = jogador.getX() - velocidade * delta; // tempo que passou X velocidade.
                float y = jogador.getY();
                jogador.setPosition(x, y);
            }
        }

        if(indoDireita) {
            // trocar imagem direita.
            jogador.setDrawable(new SpriteDrawable(new Sprite(texturaJogadorDireita)));
        } else if (indoEsquerda) {
            // trocar imagem esquerda.
            jogador.setDrawable(new SpriteDrawable(new Sprite(texturaJogadorEsquerda)));
        } else {
            // trocar imagem centro.
            jogador.setDrawable(new SpriteDrawable(new Sprite(texturaJogador)));
        }

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

        texturaJogador.dispose();
        texturaJogadorDireita.dispose();
        texturaJogadorEsquerda.dispose();
    }

}
