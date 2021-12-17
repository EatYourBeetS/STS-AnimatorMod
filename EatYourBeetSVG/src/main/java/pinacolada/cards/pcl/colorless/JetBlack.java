package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

import static com.megacrit.cardcrawl.cards.AbstractCard.CardTags.STARTER_DEFEND;

public class JetBlack extends PCLCard
{
    public static final PCLCardData DATA = Register(JetBlack.class).SetAttack(2, CardRarity.UNCOMMON, PCLAttackType.Normal, eatyourbeets.cards.base.EYBCardTarget.ALL).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.CowboyBebop);

    public JetBlack()
    {
        super(DATA);

        Initialize(8, 1, 2);
        SetUpgrade(3, 1, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Orange(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.General, 8);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        boolean shouldRetain = CheckAffinity(PCLAffinity.General) && CombatStats.TryActivateLimited(cardID);
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.BLUNT_HEAVY);
        PCLActions.Bottom.StackPower(new JetBlackPower(p, magicNumber, shouldRetain));
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        return CheckAffinity(PCLAffinity.General) && CombatStats.CanActivateLimited(cardID);
    }

    public static class JetBlackPower extends PCLPower
    {
        private boolean shouldRetain;
        public JetBlackPower(AbstractPlayer owner, int amount, boolean shouldRetain)
        {
            super(owner, JetBlack.DATA);
            this.shouldRetain = shouldRetain;

            Initialize(amount);
        }

        public void atStartOfTurn()
        {
            super.atStartOfTurn();
            if (shouldRetain) {
                shouldRetain = false;
            }
            else {
                RemovePower();
            }
        }

        @Override
        public void onUseCard(AbstractCard card, UseCardAction action)
        {
            super.onUseCard(card, action);
            if (card.hasTag(STARTER_DEFEND) && PCLGameUtilities.CanPlayTwice(card)) {
                PCLActions.Top.Callback(() -> card.use(player, (AbstractMonster)((action.target == null) ? null : action.target)));
                ReducePower(1);
            }
        }
    }
}