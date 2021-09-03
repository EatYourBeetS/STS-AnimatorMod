package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DemonFormPower;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;
import eatyourbeets.cards.animator.beta.special.BlazingHeat;
import eatyourbeets.cards.animator.series.TenseiSlime.Shizu;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.utilities.GameActions;

public class Shizu_Ifrit extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shizu_Ifrit.class)
            .SetSkill(3, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetSeries(Shizu.DATA.Series)
            .PostInitialize(data -> data.AddPreview(new BlazingHeat(), false));
    public static final int THRESHOLD = 50;

    public Shizu_Ifrit()
    {
        super(DATA);

        Initialize(0, 0, 40, 4);
        SetUpgrade(0, 0, 10);

        SetAffinity_Red(2);
        SetAffinity_Dark(2);

        SetExhaust(true);
        SetRetainOnce(true);

        SetAffinityRequirement(Affinity.Red, 4);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        final ScreenOnFireEffect effect = new ScreenOnFireEffect();
        effect.duration = effect.startingDuration = 1.5f; // Changed from 3f
        GameActions.Bottom.VFX(effect, 0.2f);
        GameActions.Bottom.StackPower(new DemonFormPower(p, 2));


        if (BurningPower.PLAYER_ATTACK_BONUS > THRESHOLD && CombatStats.TryActivateLimited(cardID))
        {
            AbstractCard c = new BlazingHeat();
            c.applyPowers();
            c.use(player, null);
        }
        else {
            GameActions.Bottom.Callback(() -> BurningPower.AddPlayerAttackBonus(magicNumber));
        }
    }
}