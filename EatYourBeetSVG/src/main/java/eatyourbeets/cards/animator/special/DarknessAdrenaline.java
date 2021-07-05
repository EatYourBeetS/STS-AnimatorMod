package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.AdrenalineEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class DarknessAdrenaline extends AnimatorCard
{
    public static final EYBCardData DATA = Register(DarknessAdrenaline.class).SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None);

    public DarknessAdrenaline()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 0, 0, 1);

        SetExhaust(true);
        SetSynergy(Synergies.Konosuba);
        SetAffinity(0, 0, 0, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.VFX(new AdrenalineEffect(), 0.15f);
        GameActions.Bottom.GainEnergy(secondaryValue);
        GameActions.Bottom.Draw(magicNumber);
    }
}