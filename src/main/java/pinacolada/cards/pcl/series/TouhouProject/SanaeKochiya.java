package pinacolada.cards.pcl.series.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.cards.pcl.replacement.Miracle;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class SanaeKochiya extends PCLCard {
    public static final PCLCardData DATA = Register(SanaeKochiya.class).SetSkill(1, CardRarity.COMMON, PCLCardTarget.None, true).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Miracle(), false));

    public SanaeKochiya() {
        super(DATA);

        Initialize(0, 0, 2, 3);
        SetUpgrade(0, 0, 1, 1);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Light(1, 0, 1);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        PCLActions.Bottom.GainTemporaryHP(magicNumber);
        PCLActions.Top.Scry(secondaryValue)
                .AddCallback(cards -> {
                            boolean hasEthereal = false;
                            boolean hasPurge = false;

                            for (AbstractCard card : cards) {
                                if (card.isEthereal) {
                                    hasEthereal = true;
                                }
                                else if (card.purgeOnUse || card.hasTag(PURGE)) {
                                    hasPurge = true;
                                }
                            }

                            if (hasEthereal) {
                                PCLActions.Top.StackPower(new NextTurnMiracle(player, 1));
                            }
                            if (hasPurge) {
                                PCLActions.Top.ApplyBlinded(TargetHelper.Enemies(), 1);
                            }
                        }
                );
    }

    public static class NextTurnMiracle extends PCLPower {
        public NextTurnMiracle(AbstractCreature owner, int amount) {
            super(owner, SanaeKochiya.DATA);
            this.amount = amount;
            updateDescription();
        }

        @Override
        public void atStartOfTurn() {
            super.atStartOfTurn();

            for (int i = 0; i < amount; i++) {
                PCLActions.Bottom.MakeCardInHand(new Miracle());
            }
            PCLActions.Bottom.RemovePower(owner, owner, this);
        }

        @Override
        public void updateDescription() {
            this.description = FormatDescription(0, amount);
            this.enabled = (amount > 0);
        }
    }
}

