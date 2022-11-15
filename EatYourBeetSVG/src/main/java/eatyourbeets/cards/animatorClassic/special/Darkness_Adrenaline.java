package eatyourbeets.cards.animatorClassic.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.AdrenalineEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class Darkness_Adrenaline extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Darkness_Adrenaline.class).SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None);

    public Darkness_Adrenaline()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 0, 0, 1);

        SetExhaust(true);
        this.series = CardSeries.Konosuba;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.VFX(new AdrenalineEffect(), 0.15f);
        GameActions.Bottom.GainEnergy(secondaryValue);
        GameActions.Bottom.Draw(magicNumber);
    }
}