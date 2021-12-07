package eatyourbeets.cards.animator.beta.colorless;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.DioBrando_TheWorld;
import eatyourbeets.cards.base.*;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class DioBrando extends AnimatorCard
{
    public static final EYBCardData DATA = Register(DioBrando.class).SetAttack(3, CardRarity.RARE).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Jojo)
            .PostInitialize(data -> data.AddPreview(new DioBrando_TheWorld(), false));

    private int turns;

    public DioBrando()
    {
        super(DATA);

        Initialize(18, 0, 3, 3);
        SetUpgrade(4, 0, 0, 0);

        SetAffinity_Red(2,0,2);
        SetAffinity_Dark(2, 0, 1);

        SetSoul(7, 0, DioBrando_TheWorld::new);
    }

    @Override
    protected void OnUpgrade()
    {
        this.AddScaling(Affinity.Red, 1);
        this.AddScaling(Affinity.Light, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY).forEach(d -> d.SetVFXColor(Color.GOLDENROD, Color.GOLDENROD).SetSoundPitch(0.5f, 1.5f));

        GameActions.Bottom.Draw(magicNumber).AddCallback(() -> {
           int attackCount = JUtils.Count(player.hand.group, c -> c.type == CardType.ATTACK);
           GameActions.Top.RecoverHP(secondaryValue * attackCount);
           GameActions.Bottom.SelectFromHand(name, player.hand.size(), false)
                    .SetOptions(true,true,true)
                    .SetMessage(GR.Common.Strings.HandSelection.MoveToDrawPile)
                    .AddCallback(cards ->
                    {
                        for (int i = cards.size() - 1; i >= 0; i--)
                        {
                            GameActions.Top.MoveCard(cards.get(i), player.hand, player.drawPile);
                        }
                    });
           cooldown.ProgressCooldownAndTrigger(attackCount, m);
        });

        cooldown.ProgressCooldownAndTrigger(m);
    }
}