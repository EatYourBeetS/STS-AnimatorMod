package eatyourbeets.cards.animator.beta.LogHorizon;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.ui.cards.DrawPileCardPreview;
import eatyourbeets.utilities.GameActions;

public class Tohya extends AnimatorCard {
    public static final EYBCardData DATA = Register(Tohya.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal);

    private final DrawPileCardPreview drawPileCardPreview = new DrawPileCardPreview(Tohya::FindMinori);

    static
    {
        DATA.AddPreview(new Minori(), false);
    }

    public Tohya() {
        super(DATA);

        Initialize(5, 0, 1, 0);
        SetUpgrade(3, 0, 0, 0);

        SetMartialArtist();

        SetSynergy(Synergies.LogHorizon);
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);

        GameActions.Bottom.Callback(this::DrawMinori);

        if (IsStarter())
        {
            GameActions.Bottom.GainBlur(magicNumber);
        }
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo)
    {
        super.calculateCardDamage(mo);

        drawPileCardPreview.SetCurrentTarget(mo);
    }

    @Override
    public void update()
    {
        super.update();

        drawPileCardPreview.Update();
    }

    @Override
    public void Render(SpriteBatch sb, boolean hovered, boolean selected, boolean library)
    {
        super.Render(sb, hovered, selected, library);

        if (!library)
        {
            drawPileCardPreview.Render(sb);
        }
    }

    private static AbstractCard FindMinori(AbstractMonster target)
    {
        AbstractCard minori = null;
        int maxDamage = Integer.MIN_VALUE;
        for (AbstractCard c : player.discardPile.group)
        {
            if (Minori.DATA.ID.equals(c.cardID))
            {
                minori = c;
            }
        }

        return minori;
    }

    private boolean DrawMinori()
    {
       AbstractCard minori = FindMinori(null);

       if (minori != null)
       {
           GameActions.Top.MoveCard(minori, player.hand)
                   .ShowEffect(true, true);
           return true;
       }

        return false;
    }
}