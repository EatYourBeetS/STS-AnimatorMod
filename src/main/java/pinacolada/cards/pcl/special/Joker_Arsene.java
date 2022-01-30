package pinacolada.cards.pcl.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.colorless.Joker;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;

public class Joker_Arsene extends PCLCard
{
    public static final PCLCardData DATA = Register(Joker_Arsene.class)
            .SetAttack(3, CardRarity.SPECIAL, PCLAttackType.Dark, PCLCardTarget.AoE)
            .SetSeries(Joker.DATA.Series);

    public Joker_Arsene()
    {
        super(DATA);

        Initialize(8, 0, 3, 1);
        SetUpgrade(3, 0);

        SetAffinity_Dark(1, 0, 3);
        SetAffinity_Blue(0, 0, 1);

        SetExhaust(true);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        return super.ModifyDamage(enemy, PCLGameUtilities.GetAffinityCount(PCLAffinity.Dark) * amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLGameEffects.Queue.RoomTint(Color.BLACK, 0.8F);
        PCLActions.Bottom.VFX(new BorderLongFlashEffect(Color.VIOLET));
        PCLActions.Bottom.VFX(new ShockWaveEffect(p.hb.cX, p.hb.cY, Color.VIOLET, ShockWaveEffect.ShockWaveType.ADDITIVE), 0.75f);
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.DARKNESS);
        TrySpendAffinity(PCLAffinity.Dark);
        if (PCLGameUtilities.GetCurrentMatchCombo() >= magicNumber && info.TryActivateLimited()) {
            for (AbstractCard c : CombatStats.GetCombatData(Joker.DATA.ID, new ArrayList<AbstractCard>())) {
                PCLActions.Bottom.PlayCard(c, PCLGameUtilities.GetRandomEnemy(true));
            };
        }
    }
}