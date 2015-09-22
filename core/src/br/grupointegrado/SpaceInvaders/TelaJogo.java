package br.grupointegrado.SpaceInvaders;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;

public class TelaJogo extends TelaBase {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Stage palco;
    private Stage palcoInformacoes;
    private BitmapFont fonte;
    private Label lbPontuacao;
    private Label lbGameOver;
    private Image jogador;
    private Texture texturaJogador;
    private Texture texturaJogadorDireita;
    private Texture texturaJogadorEsquerda;
    private boolean indoDireita;
    private boolean indoEsquerda;
    private boolean atirando;
    private Array<Image> tiros = new Array<Image>();
    private Texture texturaTiro;
    private Texture texturaMeteoro1;
    private Texture texturaMeteoro2;
    private Array<Image> meteoros1 = new Array<Image>();
    private Array<Image> meteoros2 = new Array<Image>();

    private Array<Texture> texturasExplosao = new Array<Texture>();
    private Array<Explosao> explosoes = new Array<Explosao>();

    private int pontuacao = 0;
    private boolean gameOver = false;

    private Sound somTiro;
    private Sound somExplosao;
    private Sound somGameOver;
    private Music musicaFundo;

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
        palcoInformacoes = new Stage(new FillViewport(camera.viewportWidth, camera.viewportHeight, camera));

        initSons();
        initTexturas();
        initFonte();
        initInformacoes();

        initJogador();
    }

    private void initSons() {
        somTiro = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot.mp3"));
        somExplosao = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.mp3"));
        somGameOver = Gdx.audio.newSound(Gdx.files.internal("sounds/gameover.mp3"));
        musicaFundo = Gdx.audio.newMusic(Gdx.files.internal("sounds/background.mp3"));
        musicaFundo.setLooping(true);
    }

    private void initTexturas() {
        texturaTiro = new Texture("sprites/shot.png");
        texturaMeteoro1 = new Texture("sprites/enemie-1.png");
        texturaMeteoro2 = new Texture("sprites/enemie-2.png");

        for(int i = 1; i <= 17; i++) {
            Texture text = new Texture("sprites/explosion-" + i + ".png");
            texturasExplosao.add(text);
        }
    }

    /**
     * Instância os objetos do jogador e adiciona no palco.
     */
    private void initJogador() {
        texturaJogador = new Texture("sprites/player.png");
        texturaJogadorDireita = new Texture("sprites/player-right.png");
        texturaJogadorEsquerda = new Texture("sprites/player-left.png");

        jogador = new Image(texturaJogador); // instancia ator.
        float x = camera.viewportWidth / 2 - jogador.getWidth() / 2;
        float y = 15;
        jogador.setPosition(x, y);
        palco.addActor(jogador);
    }

    /**
     * Instância os objetos de Fonte.
     */
    private void initFonte() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/roboto.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();

        param.color = Color.WHITE;
        param.size = 20;
        param.shadowOffsetX = 1;
        param.shadowOffsetY = 1;
        param.shadowColor = Color.GREEN;

        fonte = generator.generateFont(param);

        generator.dispose();
    }

    /**
     * Instância as informações escritas na tela.
     */
    private void initInformacoes() {
        Label.LabelStyle lbEstilo = new Label.LabelStyle();
        lbEstilo.font = fonte; // passa objeto fonte.
        lbEstilo.fontColor = Color.WHITE;

        lbPontuacao = new Label("0 pontos", lbEstilo);
        palcoInformacoes.addActor(lbPontuacao); // adiciona no palco.

        lbGameOver = new Label("GAME OVER !", lbEstilo);
        lbGameOver.setVisible(false);
        palcoInformacoes.addActor(lbGameOver);
    }

    /**
     * Chamado a todo a quadro de atualização do jogo (FPS - Quadros por segundos)
     * @param delta Tempo entre um quadro e outro (em segundos)
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.15f, .15f, .25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Limpa buffer de cores.

        lbPontuacao.setPosition(10, camera.viewportHeight - lbPontuacao.getPrefHeight() - 20); // altura mazima da camera - 20.
        lbPontuacao.setText(pontuacao + " ponto(s)");
        lbGameOver.setPosition(camera.viewportWidth / 2 - lbGameOver.getPrefWidth() / 2, camera.viewportHeight / 2);
        lbGameOver.setVisible(gameOver == true);

        atualizarExplosoes(delta);

        if(gameOver == false) {
            if(!musicaFundo.isPlaying()) { musicaFundo.play(); }
            capturaTeclas();
            atualizarJogador(delta);
            atualizarTiros(delta);
            atualizarMeteoros(delta);
            detectaColisoes(meteoros1, 1);
            detectaColisoes(meteoros2, 3);
        } else {
            if(musicaFundo.isPlaying()) {
                musicaFundo.stop();
            }
            reiniciarJogo();
        }

        // desenha o palco do jogo.
        palco.act(delta); // atualiza situação do palco.
        palco.draw(); // desenha palco na tela.

        // desenha o palco das informações.
        palcoInformacoes.act(delta);
        palcoInformacoes.draw();
    }

    /**
     * Verifica se o usuario pressionou ENTER para reiniciar o jogo.
     */
    private void reiniciarJogo() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            Preferences preferencias = Gdx.app.getPreferences("SpaceInvaders");
            // se não tiver pontuação retorna ZERO.
            int pontuacoMaxima = preferencias.getInteger("pontuacao_maxima", 0);

            // verifica se a pontuação atual é maior que a pontuação maxima.
            if(pontuacao > pontuacoMaxima) {
                preferencias.putInteger("pontuacao_maxima", pontuacao);
                preferencias.flush();
            }

            // volta para tela de menu.
            game.setScreen(new TelaMenu(game));
        }
    }

    private void atualizarExplosoes(float delta) {
        for(Explosao explosao : explosoes) {
            // verifica se a explosão chegou ao fim.
            if(explosao.getEstagio() >= 16) {
                explosoes.removeValue(explosao, true); // remove a explosão do array.
                explosao.getAtor().remove(); // remove o ator do palco.
            } else {
                explosao.atualizar(delta);
            }
        }
    }

    /**
     * Verifica se as teclas estão pressionadas.
     */
    private void capturaTeclas() {
        indoDireita = false;
        indoEsquerda = false;
        atirando = false;

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || clicouEsquerda() ) { indoEsquerda = true; }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || clicouDireita()) { indoDireita = true; }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.app.getType() == Application.ApplicationType.Android) {
            atirando = true;
        }
    }

    private boolean clicouDireita() {
        if(Gdx.input.isTouched()) {
            Vector3 posicao = new Vector3();
            // captura clique na janela do windows.
            posicao.x = Gdx.input.getX();
            posicao.y = Gdx.input.getY();
            // converter para uma cordenada do jogo.
            posicao = camera.unproject(posicao);

            // meio tela.
            float meio = camera.viewportWidth / 2;

            if (posicao.x > meio) {
                return true;
            }
        }
        return false;
    }

    private boolean clicouEsquerda() {
        if(Gdx.input.isTouched()) {
            Vector3 posicao = new Vector3();
            // captura clique na janela do windows.
            posicao.x = Gdx.input.getX();
            posicao.y = Gdx.input.getY();
            // converter para uma cordenada do jogo.
            posicao = camera.unproject(posicao);

            // meio tela.
            float meio = camera.viewportWidth / 2;

            if (posicao.x < meio) {
                return true;
            }
        }
        return false;
    }



    /**
     * Atualiza a posição do jogador.
     * @param delta
     */
    private void atualizarJogador(float delta) {
        float velocidade = 200; // Velocidade de movimento do jogador.

        if(indoDireita) {
            // verifica se o jogador está dentro da tela.
            if(jogador.getX() < camera.viewportWidth - jogador.getWidth()) {
                float x = jogador.getX() + velocidade * delta; // tempo que passou X velocidade.
                float y = jogador.getY();
                jogador.setPosition(x, y);
            }
        }

        if(indoEsquerda) {
            // verifica se o jogador está dentro da tela.
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

    private final float MIN_INTERVALO_TIROS = 0.3f; // minimo tempo entre os tiros.
    private float intervaloTiros = 0; // tempo acumulado entre os tiros.

    private void atualizarTiros(float delta) {
        intervaloTiros = intervaloTiros + delta; // acumula o tempo percorrido.

        // cria um novo tiro se necessário.
        if(atirando) {
            if(intervaloTiros >= MIN_INTERVALO_TIROS) { // verifica se tempo minimo foi atingido.
                Image tiro = new Image(texturaTiro);
                float x = jogador.getX() + jogador.getWidth() / 2 - tiro.getWidth() / 2;
                float y = jogador.getY() + jogador.getHeight();
                /*
                Image tiroEsquerda = new Image(texturaTiro);
                Image tiroDireita = new Image(texturaTiro);
                tiroEsquerda.setPosition(jogador.getX(), y);
                tiroDireita.setPosition(jogador.getX() + jogador.getWidth(), y);
                tiros.add(tiroEsquerda);
                tiros.add(tiroDireita);
                palco.addActor(tiroEsquerda);
                palco.addActor(tiroDireita);
                */
                tiro.setPosition(x,y);
                tiros.add(tiro);
                palco.addActor(tiro);
                intervaloTiros = 0;
                somTiro.play();
            }
        }

        float velocidade = 200; // velocidade de movimentação do tiro.
        // percorre todos os tiros existentes.
        for (Image tiro : tiros) {
            // movimento o tiro em direção ao topo.
            float x = tiro.getX();
            float y = tiro.getY() + velocidade * delta;
            tiro.setPosition(x, y);

            // remove os tiros que sairam da tela.
            if(tiro.getY() > camera.viewportHeight) {
                // remove tiro da lista.
                tiros.removeValue(tiro, true); // remove por == da lista.
                tiro.remove(); // remove do palco.
            }
        }
    }

    private void atualizarMeteoros(float delta) {

        int qtdeMeteoros = meteoros1.size + meteoros2.size; // retorna o total de meteoros criados.

        if(qtdeMeteoros < 10) {

            int tipo = MathUtils.random(1, 4); // sortei um numero aleatorio ( 1 a 4), exclui o numero 4.

            if(tipo == 1) {
                // cria meteoro 1.
                Image meteoro = new Image(texturaMeteoro1);
                // sorteia posição mas não deixa sair da tela de criação.
                float x = MathUtils.random(0, camera.viewportWidth - meteoro.getWidth());
                float y = MathUtils.random(camera.viewportHeight, camera.viewportHeight * 2);
                meteoro.setPosition(x, y);
                meteoros1.add(meteoro);
                palco.addActor(meteoro);
            } else if (tipo == 2) {
                // cria meteoro 2.
                Image meteoro = new Image(texturaMeteoro2);
                // sorteia posição mas não deixa sair da tela de criação.
                float x = MathUtils.random(0, camera.viewportWidth - meteoro.getWidth());
                float y = MathUtils.random(camera.viewportHeight, camera.viewportHeight * 2);
                meteoro.setPosition(x, y);
                meteoros2.add(meteoro);
                palco.addActor(meteoro);
            }

        }

        float velocidade1 = 100; // 200 pixels por segundo.
        for (Image meteoro : meteoros1) {
            float x = meteoro.getX();
            float y = meteoro.getY() - velocidade1 * delta;
            meteoro.setPosition(x,y); // atualiza posição do meteoro

            // remove os meteoros 1 que sairam da tela.
            if(meteoro.getY() + meteoro.getHeight() < 0) {
                // remove meteoro da lista 1.
                pontuacao = pontuacao - 2;
                meteoros1.removeValue(meteoro, true); // remove por == da lista.
                meteoro.remove(); // remove do palco.
            }
        }

        float velocidade2 = 150; // 250 pixels por segundo.
        for (Image meteoro : meteoros2) {
            float x = meteoro.getX();
            float y = meteoro.getY() - velocidade2 * delta;
            meteoro.setPosition(x,y);

            // remove os meteoros 2 que sairam da tela.
            if(meteoro.getY() + meteoro.getHeight() < 0) {
                // remove meteoro da lista 2.
                pontuacao = pontuacao - 6;
                meteoros2.removeValue(meteoro, true); // remove por == da lista.
                meteoro.remove(); // remove do palco.
            }
        }
    }

    private Rectangle recJogador = new Rectangle();
    private Rectangle recTiro = new Rectangle();
    private Rectangle recMeteoro = new Rectangle();

    private void detectaColisoes(Array<Image> meteoros, int valePonto) {
        recJogador.set(jogador.getX(), jogador.getY(), jogador.getWidth(), jogador.getHeight());

        for(Image meteoro : meteoros) {
            recMeteoro.set(meteoro.getX(), meteoro.getY(), meteoro.getWidth(), meteoro.getHeight());
            // detecta colisoes com tiros.
            for(Image tiro : tiros) {
                recTiro.set(tiro.getX(), tiro.getY(), tiro.getWidth(), tiro.getHeight());

                if(recMeteoro.overlaps(recTiro)) { // se os elementos se sobrepoem.
                    // aqui houve uma colisão do tiro com o meteoro 1.

                    pontuacao+= valePonto;
                    tiro.remove();
                    tiros.removeValue(tiro, true);

                    meteoro.remove();
                    meteoros.removeValue(meteoro, true);

                    criarExplosao(meteoro.getX() + meteoro.getWidth() / 2, meteoro.getY() + meteoro.getHeight() / 2);
                }
            }
            // detecta colisao com o player.
            if (recJogador.overlaps(recMeteoro)) {
                // ocorre colisão do jogador com o meteoro.
                gameOver = true;
                somGameOver.play();
            }
        }
    }

    /**
     * Cria a explosão na posição X e Y do meteoro.
     * @param x
     * @param y
     */
    private void criarExplosao(float x, float y) {
        Image ator = new Image(texturasExplosao.get(0));
        ator.setPosition(x - ator.getWidth() / 2, y - ator.getHeight() / 2);
        palco.addActor(ator);

        Explosao explosao = new Explosao(ator, texturasExplosao);
        explosoes.add(explosao);
        somExplosao.play();
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
        palcoInformacoes.dispose();
        fonte.dispose();

        texturaJogador.dispose();
        texturaJogadorDireita.dispose();
        texturaJogadorEsquerda.dispose();
        texturaTiro.dispose();
        texturaMeteoro1.dispose();
        texturaMeteoro2.dispose();

        for(Texture text : texturasExplosao) { text.dispose(); }

        somTiro.dispose();
        somExplosao.dispose();
        somGameOver.dispose();
        musicaFundo.dispose();
    }

}
