package eatyourbeets.cards.animator.beta;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;


public class MatouSakura extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MatouSakura.class).SetSkill(2, CardRarity.UNCOMMON);
    static
    {
        DATA.AddPreview(new MatouSakura(true), true);
    }
    private boolean transformed;
    private MatouSakura(boolean transformed)
    {
        this();
        SetTransformed(transformed);
    }

    public MatouSakura()
    {
        super(DATA);

        Initialize(0, 0, 1,2);
        SetUpgrade(0, 0, 1,1);

        SetTransformed(false);
        SetSynergy(Synergies.Fate);
    }





        @Override
        public void Refresh(AbstractMonster enemy)
        {
            super.Refresh(enemy);
        }

        @Override
        public AbstractCard makeStatEquivalentCopy()
        {
            MatouSakura other = (MatouSakura) super.makeStatEquivalentCopy();

            other.SetTransformed(transformed);

            return other;
        }

        @Override
        public void use(AbstractPlayer p, AbstractMonster m)
        {
            if (transformed)
            {

            }
            else
            {

            }
        }

        @Override
        public void renderUpgradePreview(SpriteBatch sb)
        {
            if (!transformed)
            {
                super.renderUpgradePreview(sb);
            }
        }

        @Override
        public EYBCardPreview GetCardPreview()
        {
            if (transformed)
            {
                return null;
            }

            return super.GetCardPreview();
        }

        private void SetTransformed(boolean value)
        {
            if (transformed != value)
            {
                transformed = value;

                if (transformed)
                {
                    LoadImage("Alt");

                    SetRetain(true);

                    cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0], true);
                }
                else
                {
                    LoadImage(null);

                    cardText.OverrideDescription(null, true);
                }
            }
        }
}