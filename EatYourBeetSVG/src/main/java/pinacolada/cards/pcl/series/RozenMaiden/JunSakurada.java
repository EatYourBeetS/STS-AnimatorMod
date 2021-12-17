package pinacolada.cards.pcl.series.RozenMaiden;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardTarget;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.curse.Curse_JunTormented;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class JunSakurada extends PCLCard
{
    public static final PCLCardData DATA = Register(JunSakurada.class)
    		.SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Curse_JunTormented(), false));

    public JunSakurada()
    {
        super(DATA);

        Initialize(0, 2, 2);
        SetUpgrade(0, 0, 1);
        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Dark(0,0,1);
        
        SetUnique(true, true);
        SetEthereal(true);

        SetProtagonist(true);

        SetCooldown(2, 0, this::OnCooldownCompleted);
    }

    @Override
    protected void OnUpgrade()
    {
        if (timesUpgraded % 3 == 1)
        {
            upgradeBlock(1);
        }
    }
    
    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.MakeCardInHand(new Curse_JunTormented());
        PCLActions.Bottom.StackPower(new JunSakuradaPower(p, magicNumber));
        cooldown.ProgressCooldownAndTrigger(m);
    }

    private void OnCooldownCompleted(AbstractMonster m)
    {
        PCLActions.Bottom.ModifyAllInstances(uuid, AbstractCard::upgrade)
                .IncludeMasterDeck(true)
                .IsCancellable(false);
        PCLActions.Last.Purge(this);
    }

    public static class JunSakuradaPower extends PCLPower
    {
        public JunSakuradaPower(AbstractCreature owner, int amount)
        {
            super(owner, JunSakurada.DATA);

            this.amount = amount;
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();
            PCLActions.Bottom.SelectFromPile(name, 1, player.exhaustPile)
                    .SetFilter(c -> c.type == CardType.ATTACK)
                            .AddCallback(cards -> {
                                for (AbstractCard card : cards) {
                                    AbstractCard copy = PCLGameUtilities.Imitate(card);
                                    if (copy.baseDamage > 0) {
                                        PCLGameUtilities.IncreaseDamage(copy, amount, false);
                                    }
                                    if (copy.baseBlock > 0) {
                                        PCLGameUtilities.IncreaseBlock(copy, amount, false);
                                    }
                                    PCLActions.Bottom.MakeCardInHand(copy);
                                }
                            });
            RemovePower();
        }

    }
}
