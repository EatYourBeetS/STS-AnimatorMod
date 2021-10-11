package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.Miracle;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class SanaeKochiya extends AnimatorCard {
    public static final EYBCardData DATA = Register(SanaeKochiya.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Miracle(), false));

    public SanaeKochiya() {
        super(DATA);

        Initialize(0, 0, 4, 0);
        SetUpgrade(0, 0, 1, 0);
        SetAffinity_Water(1, 0, 0);
        SetAffinity_Light(1, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Top.Scry(magicNumber)
                .AddCallback(cards -> {
                            boolean hasEthereal = false;

                            for (AbstractCard card : cards) {
                                if (card.isEthereal) {
                                    hasEthereal = true;
                                    break;
                                }
                            }

                            if (hasEthereal) {
                                GameActions.Top.StackPower(new NextTurnMiracle(player, 1));
                            }
                        }
                );
    }

    public static class NextTurnMiracle extends AnimatorPower {
        public NextTurnMiracle(AbstractCreature owner, int amount) {
            super(owner, SanaeKochiya.DATA);
            this.amount = amount;
            updateDescription();
        }

        @Override
        public void atStartOfTurn() {
            super.atStartOfTurn();

            for (int i = 0; i < amount; i++) {
                GameActions.Bottom.MakeCardInHand(new Miracle());
            }
            GameActions.Bottom.RemovePower(owner, owner, this);
        }

        @Override
        public void updateDescription() {
            this.description = FormatDescription(0, amount);
            this.enabled = (amount > 0);
        }
    }
}

