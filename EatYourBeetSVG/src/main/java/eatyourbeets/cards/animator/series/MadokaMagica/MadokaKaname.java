package eatyourbeets.cards.animator.series.MadokaMagica;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.RainbowCardEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class MadokaKaname extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MadokaKaname.class).SetSkill(2, CardRarity.RARE, EYBCardTarget.None);

    public MadokaKaname()
    {
        super(DATA);

        Initialize(0, 0, 3, 2);
        SetUpgrade(0, 0, 1, 0);
        SetHealing(true);
        SetPurge(true);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.GainTemporaryArtifact(1);
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Top.ExhaustFromPile(name, magicNumber, p.drawPile, p.hand, p.discardPile)
        .ShowEffect(true, true)
        .SetOptions(true, true)
        .SetFilter(c -> c.type == CardType.CURSE)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                GameActions.Bottom.Heal(cards.size() * secondaryValue);
                GameActions.Bottom.VFX(new BorderFlashEffect(Color.PINK, true));
                GameActions.Bottom.VFX(new RainbowCardEffect());
            }
        });
    }
}
