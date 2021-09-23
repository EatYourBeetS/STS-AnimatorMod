package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DemonFormPower;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;
import eatyourbeets.cards.animator.beta.special.BlazingHeat;
import eatyourbeets.cards.animator.series.TenseiSlime.Shizu;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.utilities.GameActions;

public class Shizu_Ifrit extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shizu_Ifrit.class)
            .SetSkill(3, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetSeries(Shizu.DATA.Series)
            .PostInitialize(data -> data.AddPreview(new BlazingHeat(), false));

    public Shizu_Ifrit()
    {
        super(DATA);

        Initialize(0, 0, 40, 2);
        SetUpgrade(0, 0, 10);

        SetAffinity_Red(2);
        SetAffinity_Dark(2);

        SetExhaust(true);
        SetRetainOnce(true);

        SetAffinityRequirement(Affinity.Red, 4);
        SetCooldown(2, 0, this::OnCooldownCompleted, true, false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        final ScreenOnFireEffect effect = new ScreenOnFireEffect();
        effect.duration = effect.startingDuration = 1.5f; // Changed from 3f
        GameActions.Bottom.VFX(effect, 0.2f);
        GameActions.Bottom.StackPower(new DemonFormPower(p, 2));
        GameActions.Bottom.Callback(() -> BurningPower.AddPlayerAttackBonus(magicNumber));


        if (AbstractDungeon.actionManager.uniqueStancesThisCombat.size() >= secondaryValue && CombatStats.TryActivateLimited(cardID)) {
            AbstractCard c = new BlazingHeat();
            c.applyPowers();
            c.use(player, null);
        }
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        if (CombatStats.TryActivateLimited(cardID)) {
            AbstractCard c = new BlazingHeat();
            c.applyPowers();
            c.use(player, null);
            GameActions.Last.Purge(this);
        }
    }
}