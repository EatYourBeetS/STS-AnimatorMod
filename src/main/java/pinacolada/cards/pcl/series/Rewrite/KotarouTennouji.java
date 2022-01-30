package pinacolada.cards.pcl.series.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.interfaces.subscribers.OnStanceChangedSubscriber;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.cards.base.cardeffects.GenericEffect;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLCombatStats;
import pinacolada.stances.PCLStance;
import pinacolada.stances.PCLStanceHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

public class KotarouTennouji extends PCLCard implements OnStanceChangedSubscriber, OnStartOfTurnPostDrawSubscriber
{
    public static final PCLCardData DATA = Register(KotarouTennouji.class).SetAttack(2, CardRarity.RARE, PCLAttackType.Normal).SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public KotarouTennouji()
    {
        super(DATA);

        Initialize(14, 0, 0);
        SetUpgrade(3, 0, 0);
        SetAffinity_Star(1, 0, 1);

        SetUnique(true, -1);
        SetProtagonist(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HEAVY);

        if (choices.TryInitialize(this))
        {
            for (PCLStanceHelper stance : PCLStanceHelper.ALL.values()) {
                choices.AddEffect(GenericEffect.EnterStance(stance));
            }
        }

        choices.Select(1, m);

        PCLCombatStats.onStartOfTurnPostDraw.Subscribe((KotarouTennouji) makeStatEquivalentCopy());
    }
    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        PCLCombatStats.onStanceChanged.Subscribe(this);
    }

    @Override
    public void OnStanceChanged(AbstractStance oldStance, AbstractStance newStance)
    {
        if (!newStance.ID.equals(NeutralStance.STANCE_ID) && player.hand.contains(this) && newStance instanceof PCLStance && CombatStats.TryActivateLimited(cardID))
        {
            PCLActions.Bottom.ObtainAffinityToken(((PCLStance) newStance).affinity, false);
            flash();
        }
    }


    @Override
    public void OnStartOfTurnPostDraw()
    {
        PCLGameEffects.Queue.ShowCardBriefly(this);
        PCLActions.Bottom.ChangeStance(NeutralStance.STANCE_ID);
        PCLCombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
    }
}