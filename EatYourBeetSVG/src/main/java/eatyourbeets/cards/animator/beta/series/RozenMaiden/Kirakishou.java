package eatyourbeets.cards.animator.beta.series.RozenMaiden;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.ui.cards.DrawPileCardPreview;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class Kirakishou extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA
    	= Register(Kirakishou.class)
    	. SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.Normal)
    	. SetColor(CardColor.COLORLESS);

    private final DrawPileCardPreview drawpilecardpreview =
            new DrawPileCardPreview(Kirakishou::FindTopCard);

    public Kirakishou()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);
        
        SetShapeshifter();

        SetCooldown(2, 0, this::OnCooldownCompleted);
        SetSynergy(Synergies.RozenMaiden);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo)
    {
        super.calculateCardDamage(mo);

        drawpilecardpreview.SetCurrentTarget(mo);
    }

    @Override
    public void update()
    {
        super.update();

        drawpilecardpreview.Update();
    }

    @Override
    public void Render(SpriteBatch sb, boolean hovered, boolean selected, boolean library)
    {
        super.Render(sb, hovered, selected, library);

        if (!library)
        {
            drawpilecardpreview.Render(sb);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.ApplyConstricted(p, m, magicNumber);

        if (p.drawPile != null && p.drawPile.size() > 0)
        {
            AbstractCard card = p.drawPile.getTopCard();
            p.drawPile.removeCard(card);

            GameActions.Top.StackPower(new KirakishouPower(m, card));
        }

        cooldown.ProgressCooldownAndTrigger(m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Last.Callback(() ->
        {
            for (AbstractMonster mo : GameUtilities.GetEnemies(true))
            {
                ArrayList<AbstractPower> mp = mo.powers;
                for (AbstractPower mpi : mp)
                {
                    KirakishouPower stasis = JUtils.SafeCast(mpi, KirakishouPower.class);
                    if (stasis != null)
                        stasis.Release();
                }
            }
        });
    }

    private static AbstractCard FindTopCard(AbstractMonster Target)
    {
        CardGroup drawPile = player.drawPile;

        if (drawPile.size() == 0)
            return null;
        else
            return drawPile.getTopCard();
    }

    public static class KirakishouPower extends AnimatorPower
    {
//        public static final String POWER_ID = GR.Common.CreateID(KirakishouPower.class.getSimpleName());
//        public static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(StasisPower.POWER_ID);

        private final ArrayList<AbstractCard> cards = new ArrayList<>();

        public KirakishouPower(AbstractCreature owner, AbstractCard card)
        {
            super(owner, Kirakishou.DATA);

            this.cards.add(card);

            updateDescription();
        }

        public KirakishouPower(AbstractCreature owner, ArrayList<AbstractCard> cards)
        {
            super(owner, Kirakishou.DATA);

            this.cards.addAll(cards);

            updateDescription();
        }

        @Override
        public void updateDescription()
        {
            if (cards == null || cards.size() == 0)
            {
                description = FormatDescription(0);
            }
            else if (cards.size() == 1)
            {
                description = FormatDescription(1, cards.get(0).name);
            }
            else
            {
                StringBuilder pulled_list = new StringBuilder();
                pulled_list.append(cards.get(0).name);
                for (int i = 1; i < cards.size() - 1; i ++)
                {
                    pulled_list.append(", ").append(cards.get(i).name);
                }
                pulled_list.append(" and ").append(cards.get(cards.size() - 1).name);

                description = FormatDescription(2, pulled_list.toString());
            }
        }

        public void Release()
        {
            GameActions.Top.RemovePower(this.owner, this.owner, this);

            for (AbstractCard card : this.cards)
            {
                GameUtilities.ModifyCostForTurn(card, -1, true);
                GameActions.Bottom.MakeCardInHand(card);
            }
        }

        @Override
        public void onDeath()
        {
            for (AbstractCard card : this.cards)
            {
                GameActions.Bottom.MakeCardInHand(card);
            }
            super.onDeath();
        }

        @Override
        public AbstractPower makeCopy()
        {
            ArrayList<AbstractCard> other = new ArrayList<>();
            for (AbstractCard card : this.cards)
                other.add(card.makeStatEquivalentCopy());
            return new KirakishouPower(owner, other);
        }

        @Override
        public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
        {
            KirakishouPower other = JUtils.SafeCast(power, KirakishouPower.class);
            if (other != null && power.owner == target)
            {
                this.cards.addAll(other.cards);

                updateDescription();
            }

            super.onApplyPower(power, target, source);
        }
    }
}
