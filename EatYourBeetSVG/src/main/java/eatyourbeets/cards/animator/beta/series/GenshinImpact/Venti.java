package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.actions.defect.DecreaseMaxOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.orbs.animator.Aether;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Venti extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber {
    public static final EYBCardData DATA = Register(Venti.class).SetSkill(2, CardRarity.RARE, EYBCardTarget.None);

    public Venti() {
        super(DATA);

        Initialize(0, 0, 2, 4);
        SetUpgrade(0, 0, 0, 2);

        SetEthereal(true);
        SetShapeshifter();
        SetSynergy(Synergies.GenshinImpact);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (player.maxOrbs > 0) {
            GameActions.Bottom.Add(new DecreaseMaxOrbAction(player.maxOrbs));
        }

    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.VFX(new WhirlwindEffect(), 0f);

        GameActions.Bottom.ChannelOrbs(Aether::new, magicNumber);

        if (CombatStats.OrbsEvokedThisCombat().size() > 0) {
            Venti other = (Venti) makeStatEquivalentCopy();
            CombatStats.onStartOfTurnPostDraw.Subscribe(other);
        }

    }

    @Override
    public void OnStartOfTurnPostDraw() {
        int orbCount = CombatStats.OrbsEvokedThisCombat().size();
        if (orbCount > 0) {
            GameEffects.Queue.ShowCardBriefly(this);

            int startIdx = Math.max(orbCount - secondaryValue,0);

            for (int i = startIdx; i < orbCount; i++) {
                CombatStats.OrbsEvokedThisCombat().get(i).onEndOfTurn();
            }

            CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        }
    }
}