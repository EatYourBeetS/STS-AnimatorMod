package eatyourbeets.cards.animator.series.MadokaMagica;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.HPAttribute;
import eatyourbeets.utilities.*;

public class MadokaKaname extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MadokaKaname.class)
            .SetSkill(2, CardRarity.RARE, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    private static final int HEAL_AMOUNT = 3;

    public MadokaKaname()
    {
        super(DATA);

        Initialize(0, 0, 3, 0);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Blue(1);
        SetAffinity_Light(2);

        SetHealing(true);
        SetPurge(true);
        SetProtagonist(true);
        SetHarmonic(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return secondaryValue <= 0 ? null : HPAttribute.Instance.SetCard(this, false).SetText(new ColoredString(secondaryValue, Colors.Cream(1f)));
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        GameUtilities.ModifySecondaryValue(this,
        JUtils.Count(player.drawPile.group, c -> c.type == CardType.CURSE) +
        JUtils.Count(player.discardPile.group, c -> c.type == CardType.CURSE) +
        JUtils.Count(player.hand.group, c -> c.type == CardType.CURSE), false);
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c)
    {
        super.triggerOnOtherCardPlayed(c);

        if (player.hand.contains(this) && (GameUtilities.GetAffinityLevel(c, Affinity.Blue, true) >= 2 || GameUtilities.GetAffinityLevel(c, Affinity.Light, true) >= 2))
        {
            GameActions.Bottom.GainTemporaryHP(1);
            GameActions.Bottom.Flash(this);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ExhaustFromPile(name, magicNumber, p.drawPile, p.hand, p.discardPile)
        .ShowEffect(true, true)
        .SetOptions(true, true)
        .SetFilter(c -> c.type == CardType.CURSE)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                for (int i = 0; i < cards.size(); i++)
                {
                    GameActions.Bottom.Heal(HEAL_AMOUNT);
                }
                GameActions.Bottom.VFX(new BorderFlashEffect(Color.PINK, true));
            }
        });
    }
}
