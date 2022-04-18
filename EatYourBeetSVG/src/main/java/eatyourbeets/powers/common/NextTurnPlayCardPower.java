package eatyourbeets.powers.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.TupleT2;

import java.util.ArrayList;
import java.util.StringJoiner;

public class NextTurnPlayCardPower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(NextTurnPlayCardPower.class);

    protected final ArrayList<TupleT2<AbstractCard, AbstractMonster>> cards = new ArrayList<>();

    public NextTurnPlayCardPower(AbstractCreature owner, AbstractCard card, AbstractMonster target)
    {
        this(owner, JUtils.CreateList(card), target);
    }

    public NextTurnPlayCardPower(AbstractCreature owner, ArrayList<AbstractCard> cards, AbstractMonster target)
    {
        super(owner, POWER_ID);

        this.canBeZero = true;

        for (AbstractCard c : cards)
        {
            this.cards.add(new TupleT2<>(c, target));
        }

        Initialize(0, PowerType.BUFF, true);
    }

    protected NextTurnPlayCardPower(NextTurnPlayCardPower power)
    {
        super(power.owner, POWER_ID);

        this.canBeZero = true;
        this.cards.addAll(power.cards);

        Initialize(0, PowerType.BUFF, true);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        this.amount = cards.size();
    }

    @Override
    public void updateDescription()
    {
        final StringJoiner sj = new StringJoiner(", ");
        for (TupleT2<AbstractCard, AbstractMonster> pair : cards)
        {
            sj.add(JUtils.ModifyString(pair.V1.name, w -> "#y" + w));
        }

        this.description = FormatDescription(0) + " NL " + sj.toString();
    }

    @Override
    protected void OnSamePowerApplied(AbstractPower power)
    {
        super.OnSamePowerApplied(power);

        this.cards.addAll(((NextTurnPlayCardPower) power).cards);
        this.amount = cards.size();
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new NextTurnPlayCardPower(this);
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        for (TupleT2<AbstractCard, AbstractMonster> pair : cards)
        {
            final AbstractCard c = pair.V1;
            GameUtilities.ResetVisualProperties(c);
            c.target_x = owner.drawX;
            c.target_y = owner.drawY;
            GameActions.Bottom.PlayCard(c, pair.V2)
            .SpendEnergy(false, true)
            .SetPurge(true);
        }
        cards.clear();
        RemovePower();
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c)
    {
        super.renderIcons(sb, x, y, c);

        for (int i = 0; i < cards.size(); i++)
        {
            final AbstractCard card = cards.get(i).V1;
            card.drawScale = card.targetDrawScale = 0.3f;
            card.transparency = card.targetTransparency = 0.75f;
            card.angle = card.targetAngle = 0;
            card.fadingOut = false;
            card.current_x = card.target_x = AbstractDungeon.overlayMenu.energyPanel.current_x + (AbstractCard.IMG_WIDTH * (0.4f + (0.1f * i)));
            card.current_y = card.target_y = AbstractDungeon.overlayMenu.energyPanel.current_y;
            card.render(sb);
        }
    }
}
