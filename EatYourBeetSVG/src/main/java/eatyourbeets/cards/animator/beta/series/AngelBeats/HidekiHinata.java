package eatyourbeets.cards.animator.beta.series.AngelBeats;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.ui.cards.TargetEffectPreview;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class HidekiHinata extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HidekiHinata.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged).SetSeriesFromClassPackage();
    private final TargetEffectPreview targetEffectPreview = new TargetEffectPreview(this::OnTargetChanged);
    private boolean showDamage = false;

    public HidekiHinata()
    {
        super(DATA);

        Initialize(7, 0, 1, 3);
        SetUpgrade(2, 0, 0, 0);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(1, 0, 0);
        SetExhaust(true);
        AfterLifeMod.Add(this);
    }

    @Override
    public void update()
    {
        super.update();

        targetEffectPreview.Update();
        if (showDamage) {
            GameUtilities.IncreaseHitCount(this, 1, true);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.GUNSHOT);

        if (! GameUtilities.IsAttacking(m.intent)) {
            GameActions.Bottom.GainSupportDamage(secondaryValue);
        }

        if (CombatStats.ControlPile.Contains(this)) {
            GameActions.Bottom.StackPower(new EnergizedPower(p, 1));
        }
    }

    private void OnTargetChanged(AbstractMonster monster)
    {
        showDamage = (monster == null || !GameUtilities.IsAttacking(monster.intent));
    }
}