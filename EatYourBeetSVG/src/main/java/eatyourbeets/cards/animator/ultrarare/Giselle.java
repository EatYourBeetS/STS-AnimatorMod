package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FireBreathingPower;
import com.megacrit.cardcrawl.vfx.combat.FlameBarrierEffect;
import eatyourbeets.cards.animator.special.OrbCore_Fire;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.TargetHelper;

public class Giselle extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(Giselle.class)
            .SetSkill(2, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.GATE);

    public Giselle()
    {
        super(DATA);

        Initialize(0, 0, 4);
        SetUpgrade(0, 0, 2);

        SetAffinity_Red(2);
        SetAffinity_Dark(2);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.VFX(new FlameBarrierEffect(m.hb.cX, m.hb.cY), 0.5f);
        GameActions.Bottom.TryChooseSpendAffinity(name, CombatStats.Affinities.GetAffinityLevel(Affinity.General, true)).SetOptions(true, false).AddConditionalCallback(
                cards -> {
                    if (cards.size() > 0) {
                        GameActions.Bottom.StackPower(new FireBreathingPower(player, cards.get(0).magicNumber));
                    }
                }
        );
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        super.triggerOnEndOfTurnForPlayingCard();
        GameActions.Bottom.ApplyBurning(TargetHelper.Enemies(), magicNumber);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (startOfBattle)
        {
            GameEffects.List.ShowCopy(this);
            AbstractCard c = new OrbCore_Fire();
            c.applyPowers();
            c.use(player, null);
        }
    }
}