package eatyourbeets.cards.unnamed.common;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.effects.vfx.megacritCopy.DivinityParticleEffect2;
import eatyourbeets.utilities.GameActions;

public class Intimidate extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Intimidate.class)
            .SetSkill(1, CardRarity.COMMON);

    public Intimidate()
    {
        super(DATA);

        Initialize(0, 0, 2, 10);
        SetUpgrade(0, 0, 0, 4);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        for (int i = 0; i < 6; i++)
        {
            GameActions.Bottom.VFX(new DivinityParticleEffect2(m).SetColor(0.8f, 1f, 0.2f, 0.3f, 0.2f, 0.3f), 0f, false);
        }
        GameActions.Bottom.StackAmplification(p, m, secondaryValue).ShowEffect(true, true);
        GameActions.Bottom.Draw(magicNumber);
    }
}