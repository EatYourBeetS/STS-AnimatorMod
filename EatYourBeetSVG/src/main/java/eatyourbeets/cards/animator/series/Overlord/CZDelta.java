package eatyourbeets.cards.animator.series.Overlord;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.interfaces.subscribers.OnAttackSubscriber;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class CZDelta extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber, OnAttackSubscriber
{
    public static final EYBCardData DATA = Register(CZDelta.class)
            .SetAttack(0, CardRarity.COMMON, EYBAttackType.Ranged, EYBCardTarget.Normal)
            .SetSeriesFromClassPackage();

    private static final Color VFX_COLOR = new Color(0.6f, 1f, 0.6f, 1f);

    public CZDelta()
    {
        super(DATA);

        Initialize(9, 0, 2);
        SetUpgrade(0, 0, -1);

        SetAffinity_Green(1, 1, 2);
        SetAffinity_Orange(1, 0, 0);

    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.GUNSHOT).forEach(d -> d
                .SetDamageEffect(c ->
                {
                    SFX.Play(SFX.ATTACK_MAGIC_BEAM_SHORT, 0.9f, 1.1f, 0.95f);
                    return GameEffects.List.Add(VFX.SmallLaser(player.hb, c.hb, VFX_COLOR, 0.1f)).duration * 0.7f;
                })
                .SetSoundPitch(1.5f, 1.55f)
                .SetVFXColor(VFX_COLOR));

        CombatStats.onStartOfTurnPostDraw.Subscribe((CZDelta) makeStatEquivalentCopy());
    }

    @Override
    public void OnStartOfTurnPostDraw() {
        GameActions.Bottom.Callback(() ->
        {
            GameEffects.Queue.ShowCardBriefly(makeStatEquivalentCopy());
            GameActions.Bottom.StackPower(TargetHelper.Player(), PowerHelper.TemporaryDexterity, -magicNumber);
            CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        });
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle) {
        super.triggerWhenCreated(startOfBattle);

        if (CombatStats.CanActivateLimited(cardID)) {
            CombatStats.onAttack.Subscribe(this);
        }
    }

    @Override
    public void OnAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (!CombatStats.CanActivateLimited(cardID)) {
            CombatStats.onAttack.Unsubscribe(this);
        }
        else if (!player.hand.contains(this) && GameUtilities.IsMonster(target) && GameUtilities.GetHealthPercentage(target) < 0.1f && CombatStats.TryActivateLimited(cardID)) {
            GameActions.Last.MoveCard(this, player.hand)
                    .AddCallback(c -> ((CZDelta)c).SetPurge(true, true));
            CombatStats.onAttack.Unsubscribe(this);
        }
    }
}