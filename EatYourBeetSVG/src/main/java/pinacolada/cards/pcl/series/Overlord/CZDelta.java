package pinacolada.cards.pcl.series.Overlord;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnAttackSubscriber;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.effects.VFX;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class CZDelta extends PCLCard implements OnStartOfTurnPostDrawSubscriber, OnAttackSubscriber
{
    public static final PCLCardData DATA = Register(CZDelta.class)
            .SetAttack(0, CardRarity.COMMON, PCLAttackType.Ranged, eatyourbeets.cards.base.EYBCardTarget.Normal)
            .SetSeriesFromClassPackage();

    private static final Color VFX_COLOR = new Color(0.6f, 1f, 0.6f, 1f);

    public CZDelta()
    {
        super(DATA);

        Initialize(9, 0, 2);
        SetUpgrade(1, 0, -1);

        SetAffinity_Green(1, 0, 2);
        SetAffinity_Orange(1, 0, 0);

    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.GUNSHOT).forEach(d -> d
                .SetDamageEffect(c ->
                {
                    SFX.Play(SFX.ATTACK_MAGIC_BEAM_SHORT, 0.9f, 1.1f, 0.95f);
                    return PCLGameEffects.List.Add(VFX.SmallLaser(player.hb, c.hb, VFX_COLOR, 0.1f)).duration * 0.7f;
                })
                .SetSoundPitch(1.5f, 1.55f)
                .SetVFXColor(VFX_COLOR));

        PCLCombatStats.onStartOfTurnPostDraw.Subscribe((CZDelta) makeStatEquivalentCopy());
    }

    @Override
    public void OnStartOfTurnPostDraw() {
        PCLActions.Bottom.Callback(() ->
        {
            PCLGameEffects.Queue.ShowCardBriefly(makeStatEquivalentCopy());
            PCLActions.Bottom.StackPower(TargetHelper.Player(), PCLPowerHelper.TemporaryDexterity, -magicNumber);
            PCLCombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        });
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle) {
        super.triggerWhenCreated(startOfBattle);

        if (CombatStats.CanActivateLimited(cardID)) {
            PCLCombatStats.onAttack.Subscribe(this);
        }
    }

    @Override
    public void OnAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (!CombatStats.CanActivateLimited(cardID)) {
            PCLCombatStats.onAttack.Unsubscribe(this);
        }
        else if (!player.hand.contains(this) && PCLGameUtilities.IsMonster(target) && PCLGameUtilities.GetHealthPercentage(target) < 0.1f && CombatStats.TryActivateLimited(cardID)) {
            PCLActions.Last.MoveCard(this, player.hand);
            PCLCombatStats.onAttack.Unsubscribe(this);
        }
    }
}