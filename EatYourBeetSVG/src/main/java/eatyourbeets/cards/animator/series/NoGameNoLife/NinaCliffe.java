package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.actions.basic.MoveCard;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class NinaCliffe extends AnimatorCard
{
    public static final EYBCardData DATA = Register(NinaCliffe.class)
            .SetSkill(3, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .ObtainableAsReward((data, deck) -> deck.size() >= (25 + (10 * data.GetTotalCopies(deck))));

    public NinaCliffe()
    {
        super(DATA);

        Initialize(0, 2, 2, 5);

        SetAffinity_Blue(2, 0, 4);
        SetAffinity_Green(1);

        SetExhaust(true);
        SetDelayed(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetDelayed(false);
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return super.GetBlockInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.GainBlock(block);
        }

        GameActions.Bottom.PurgeFromPile(name, secondaryValue, p.drawPile, p.hand, p.discardPile)
        .SetOptions(false, false)
        .SetFilter(GameUtilities::IsLowCost)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                GameActions.Bottom.StackPower(new NinaCliffePower(player, cards));
            }
        });
    }

    public static class NinaCliffePower extends AnimatorPower
    {
        private final CardGroup cards;

        public NinaCliffePower(AbstractCreature owner, ArrayList<AbstractCard> cards)
        {
            super(owner, NinaCliffe.DATA);

            this.cards = GameUtilities.CreateCardGroup(cards);
            this.cards.shuffle(rng);

            Initialize(this.cards.size());
        }

        @Override
        public void updateDescription()
        {
            if (cards.size() > 0)
            {
                this.description = FormatDescription(0, JUtils.ModifyString(cards.group.get(0).name, w -> "#y" + w));
            }
            else
            {
                this.description = "";
            }
        }

        @Override
        protected void OnSamePowerApplied(AbstractPower power)
        {
            super.OnSamePowerApplied(power);

            final NinaCliffePower other = (NinaCliffePower) power;
            if (other != null)
            {
                final AbstractCard temp = cards.size() > 0 ? cards.group.remove(0) : null;

                this.cards.group.addAll(other.cards.group);
                this.cards.shuffle(rng);

                if (temp != null)
                {
                    this.cards.group.add(0, temp);
                }

                RefreshAmount();
            }
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            for (int i = 0; i < cards.group.size(); i++)
            {
                final AbstractCard c = cards.group.get(i);
                if (!CombatStats.PurgedCards.contains(c))
                {
                    cards.group.remove(c);

                    if (!RefreshAmount())
                    {
                        return;
                    }
                }
            }

            final AbstractCard toPlay = cards.group.remove(0);
            toPlay.target_x = MoveCard.DEFAULT_CARD_X_LEFT;
            toPlay.target_y = MoveCard.DEFAULT_CARD_Y;
            GameActions.Last.PlayCard(toPlay, CombatStats.PurgedCards, null);
            RefreshAmount();
            flash();
        }

        private boolean RefreshAmount()
        {
            if (amount > cards.size())
            {
                reducePower(amount - cards.size());
            }
            else if (amount > cards.size())
            {
                stackPower(cards.size() - amount);
            }

            if (amount <= 0)
            {
                RemovePower();
                return false;
            }

            return true;
        }

        @Override
        public void renderIcons(SpriteBatch sb, float x, float y, Color c)
        {
            super.renderIcons(sb, x, y, c);

            final AbstractCard card = cards.size() > 0 ? cards.group.get(0) : null;
            if (card != null)
            {
                card.drawScale = card.targetDrawScale = 0.3f;
                card.transparency = card.targetTransparency = 0.75f;
                card.angle = card.targetAngle = 0;
                card.fadingOut = false;
                card.current_x = AbstractDungeon.overlayMenu.energyPanel.current_x + (AbstractCard.IMG_WIDTH * 0.4f);
                card.current_y = AbstractDungeon.overlayMenu.energyPanel.current_y;
                card.render(sb);
            }
        }
    }
}