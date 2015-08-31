package br.grupointegrado.SpaceInvaders;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;

public class Explosao {

    private static float tempo_troca = 1f / 17; // um segundo para trocar as imagens. Cada com aproximadamente 0,05 segundos.

    private int estagio = 0; // controla o estagio do 0 ao 16.
    private Array<Texture> texturas;
    private Image ator;
    private float tempoAcumulado = 0;

    public Explosao (Image ator, Array<Texture> texturas) {
        this.ator = ator;
        this.texturas = texturas;
    }

    /**
     * Calcula o tempo acumulado e realiza a troca de estágio da explosão.
     * Exemplo
     * quadro -> 0,016 segundos.
     * imagem -> 0,05 segundos.
     */
    public void atualizar(float delta) {
        tempoAcumulado = tempoAcumulado + delta;
        if(tempoAcumulado >= tempo_troca) {
            tempoAcumulado = 0;
            estagio++;
            Texture textura = texturas.get(estagio);
            ator.setDrawable(new SpriteDrawable(new Sprite(textura)));
        }
    }

    public int getEstagio () { return estagio; }

    public Image getAtor() { return ator; }

}
