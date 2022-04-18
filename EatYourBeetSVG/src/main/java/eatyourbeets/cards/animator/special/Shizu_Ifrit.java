package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;
import eatyourbeets.cards.animator.series.TenseiSlime.Shizu;
import eatyourbeets.cards.animator.status.Status_Burn;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.stances.CorruptionStance;
import eatyourbeets.utilities.GameActions;

public class Shizu_Ifrit extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shizu_Ifrit.class)
            .SetSkill(2, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetSeries(Shizu.DATA.Series)
            .PostInitialize(data -> data.AddPreview(new Status_Burn(true), false));

    public Shizu_Ifrit()
    {
        super(DATA);

        Initialize(0, 0, 40);
        SetUpgrade(0, 0, 10);

        SetAffinity_Red(2);
        SetAffinity_Dark(2);

        SetExhaust(true);
        SetRetainOnce(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        final ScreenOnFireEffect effect = new ScreenOnFireEffect();
        effect.duration = effect.startingDuration = 1.5f; // Changed from 3f
        GameActions.Bottom.VFX(effect, 0.2f);
        GameActions.Bottom.MakeCardInHand(new Status_Burn(true));
        GameActions.Bottom.MakeCardInHand(new Status_Burn(true));
        GameActions.Bottom.Callback(() -> BurningPower.AddPlayerAttackBonus(magicNumber));
        GameActions.Bottom.ChangeStance(CorruptionStance.STANCE_ID);
    }
}