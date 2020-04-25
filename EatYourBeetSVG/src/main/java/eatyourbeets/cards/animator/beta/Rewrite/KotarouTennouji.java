package eatyourbeets.cards.animator.beta.Rewrite;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.subscribers.OnBattleStartSubscriber;
import eatyourbeets.interfaces.subscribers.OnStanceChangedSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

public class KotarouTennouji extends AnimatorCard implements OnBattleStartSubscriber, OnStanceChangedSubscriber {
    public static final EYBCardData DATA = Register(KotarouTennouji.class).SetAttack(3, CardRarity.RARE, EYBAttackType.Normal);

    public KotarouTennouji() {
        super(DATA);

        Initialize(14, 0, 0);
        SetUpgrade(3, 0, 0);
        SetScaling(1,1,1);

        SetShapeshifter();
        SetUnique(true,true);

        SetSynergy(Synergies.Rewrite);

        if (CanSubscribeToEvents())
        {
            OnBattleStart();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);

        EnterRandomStanceNotCurrent();
    }

    @Override
    public void OnBattleStart() {
        CombatStats.onStanceChanged.Subscribe(this);
    }

    @Override
    public void OnStanceChanged(AbstractStance oldStance, AbstractStance newStance)
    {
        if (player.hand.contains(this) && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.ModifyAllInstances(uuid, AbstractCard::upgrade)
                    .IncludeMasterDeck(true)
                    .IsCancellable(false);
            flash();
        }
    }

    private void EnterRandomStanceNotCurrent()
    {
        RandomizedList<String> stances = new RandomizedList();

        if (!player.stance.ID.equals(ForceStance.STANCE_ID)) {
            stances.Add(ForceStance.STANCE_ID);
        }

        if (!player.stance.ID.equals(AgilityStance.STANCE_ID)) {
            stances.Add(AgilityStance.STANCE_ID);
        }

        if (!player.stance.ID.equals(IntellectStance.STANCE_ID)) {
            stances.Add(IntellectStance.STANCE_ID);
        }

        if (!player.stance.ID.equals(NeutralStance.STANCE_ID)) {
            stances.Add(NeutralStance.STANCE_ID);
        }

        GameActions.Bottom.ChangeStance(stances.Retrieve(rng));
    }
}