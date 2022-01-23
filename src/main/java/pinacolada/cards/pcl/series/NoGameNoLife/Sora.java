package pinacolada.cards.pcl.series.NoGameNoLife;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.cards.pcl.special.Sora_Strategy1;
import pinacolada.cards.pcl.special.Sora_Strategy2;
import pinacolada.cards.pcl.special.Sora_Strategy3;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Sora extends PCLCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final PCLCardData DATA = Register(Sora.class)
            .SetSkill(1, CardRarity.RARE, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetSeries(CardSeries.NoGameNoLife)
            .PostInitialize(data -> data
                    .AddPreview(new Sora_Strategy1(), true)
                    .AddPreview(new Sora_Strategy2(), true)
                    .AddPreview(new Sora_Strategy3(), true)
                    .AddPreview(new Shiro(), false));
    private AbstractCard plan;

    public Sora()
    {
        super(DATA);

        Initialize(0, 1, 1, 2);

        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Orange(1);

        SetProtagonist(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.GainTemporaryHP(magicNumber);
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        group.addToBottom(new Sora_Strategy1());
        group.addToBottom(new Sora_Strategy2());
        group.addToBottom(new Sora_Strategy3());

        if (upgraded) {
            for (AbstractCard c : group.group) {
                c.upgrade();
            }
        }

        PCLActions.Bottom.SelectFromPile(name, 1, group)
                .SetOptions(false, false)
                .AddCallback(cards ->
                {
                    if (cards.size() > 0)
                    {
                        PCLCombatStats.onStartOfTurnPostDraw.Subscribe(this);
                        plan = cards.get(0);
                    }
                });

        if (info.GetPreviousCardID().equals(Shiro.DATA.ID) && info.TryActivateLimited())
        {
            PCLActions.Bottom.GainEnergyNextTurn(secondaryValue);
        }
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        if (plan != null) {
            PCLActions.Bottom.MakeCardInHand(plan).SetUpgrade(upgraded, false);
            PCLGameUtilities.RefreshHandLayout();
        }
        PCLCombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        final AbstractCard last = PCLGameUtilities.GetLastCardPlayed(true);
        return last != null && Shiro.DATA.ID.equals(last.cardID);
    }
}