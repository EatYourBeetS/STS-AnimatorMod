package eatyourbeets.cards.unnamed.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.GameActions;

public class Experiment extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Experiment.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.Minion);

    public Experiment()
    {
        super(DATA);

        Initialize(0, 0, 2, 4);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.VFX(VFX.PotionBounce(hb, m.hb), 0.75f, true);
        GameActions.Bottom.ApplyPoison(p, m, secondaryValue);
        GameActions.Bottom.GainStrength(p, m, magicNumber);
        GameActions.Bottom.GainDexterity(p, m, magicNumber);
    }
}