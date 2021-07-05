package eatyourbeets.cards.animator.series.MadokaMagica;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class MadokaKaname extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MadokaKaname.class).SetSkill(2, CardRarity.RARE, EYBCardTarget.None);

    private static final int HEAL_AMOUNT = 3;

    public MadokaKaname()
    {
        super(DATA);

        Initialize(0, 0, 3, 0);
        SetUpgrade(0, 0, 1, 0);
        SetHealing(true);
        SetPurge(true);

        SetSynergy(Synergies.MadokaMagica);
        SetAffinity(0, 0, 1, 2, 0);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
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

        if (player.hand.contains(this) && c.hasTag(SPELLCASTER))
        {
            GameActions.Bottom.GainTemporaryHP(1);
            GameActions.Bottom.Flash(this);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
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
