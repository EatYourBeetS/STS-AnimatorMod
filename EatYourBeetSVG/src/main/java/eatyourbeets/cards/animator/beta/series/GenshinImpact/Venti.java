package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.actions.defect.DecreaseMaxOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.orbs.animator.Aether;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.TargetHelper;

import java.util.ArrayList;

public class Venti extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber {
    public static final EYBCardData DATA = Register(Venti.class).SetSkill(2, CardRarity.RARE, EYBCardTarget.None);
    protected final ArrayList<AbstractOrb> nextTurnOrbs = new ArrayList<>();

    public Venti() {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 0);

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

        int idxStart = p.filledOrbCount() - p.maxOrbs;
        int orbsEvoked = magicNumber + idxStart;
        for (int i = Math.max(idxStart,0); i < orbsEvoked; i++)
        {
            AbstractOrb orb = p.orbs.get(i);
            if (!(orb instanceof EmptyOrbSlot)) {
                nextTurnOrbs.add(orb.makeCopy());
            }
        }
        GameActions.Bottom.ChannelOrbs(Aether::new, magicNumber);
        if (orbsEvoked > 0) {
            GameActions.Bottom.DiscardFromHand(name, orbsEvoked, !this.upgraded);
            GameActions.Bottom.Draw(orbsEvoked);
        }

        if (nextTurnOrbs.size() > 0) {
            Venti other = (Venti) makeStatEquivalentCopy();
            other.nextTurnOrbs.addAll(this.nextTurnOrbs);
            CombatStats.onStartOfTurnPostDraw.Subscribe(other);
        }

    }

    @Override
    public void OnStartOfTurnPostDraw() {
        if (this.nextTurnOrbs.size() > 0) {
            GameEffects.Queue.ShowCardBriefly(this);

            for (AbstractOrb orb : this.nextTurnOrbs) {
                orb.onStartOfTurn();
                orb.onEndOfTurn();
            }

            CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        }
    }
}